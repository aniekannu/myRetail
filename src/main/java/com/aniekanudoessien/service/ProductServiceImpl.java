package com.aniekanudoessien.service;

import com.aniekanudoessien.exception.*;
import com.aniekanudoessien.model.*;
import com.aniekanudoessien.model.dbdocument.Price;
import com.aniekanudoessien.model.redskyproduct.CatalogProduct;
import com.aniekanudoessien.model.responseproduct.ProductInfo;
import com.aniekanudoessien.repository.PriceRepository;
import com.aniekanudoessien.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Config config;


    // this method adds a new document to the database comprising of a product id and price
    // field, both composed in a Price object. A ProductInfo entity is return to the client
    @Override
    public ProductInfo setPrice(PriceChange priceChange) throws RestClientException, ProductAlreadyExistsException {
        Price createPrice = new Price();
        ProductInfo createdProductInfo = null;
        String stringId = String.valueOf(priceChange.getProductId());
        String url = String.format(config.getRedskyUrl(), stringId);
        try{
            // call to the product catalog
            ResponseEntity<CatalogProduct> response =  restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<CatalogProduct>(){});
            CatalogProduct redskyProduct = response.getBody();
            String productId = redskyProduct.getProduct().getItem().getProductId();

            // product id value retrieved from the call to catalog response is used to
            // check if a document with that same value exists in the database
            Price priceInfo = priceRepository.findByProductId(Long.valueOf(productId));
            if(priceInfo != null){
                throw new ProductAlreadyExistsException("Product with id " + stringId + " already exits");
            }
            createPrice.setProductId(Long.valueOf(redskyProduct.getProduct().getItem().getProductId()));
            createPrice.setValue(priceChange.getValue());
            priceRepository.save(createPrice);

            // the fields in the client bound response object are set with values from the catalog
            // response entity and pricing data provided by the client and Currency enumerated class
            createdProductInfo = getProductInfo(redskyProduct, createPrice);
        } catch(RestClientException ex){
            throw new RestClientException("Product with id " + stringId + " is invalid");
        } catch(ProductAlreadyExistsException exc){
            throw new ProductAlreadyExistsException(exc.getMessage());
        }
        return createdProductInfo;
    }

    // this method removes a Price document from our database and breaks the link to Redsky Catalog
    @Override
    public String deleteProduct(Long id) throws RestClientException, ProductNotFoundException{
        ProductInfo productInfo = null;
        String stringId = String.valueOf(id);
        String url = String.format(config.getRedskyUrl(), stringId);

        try {
            ResponseEntity<CatalogProduct> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<CatalogProduct>() {
            });

            CatalogProduct catalogProduct = response.getBody();
            String productId = catalogProduct.getProduct().getItem().getProductId();
            Price priceInfo = priceRepository.findByProductId(Long.valueOf(productId));
            if (priceInfo == null) {
                throw new ProductNotFoundException("Product with id " + stringId + " could not be found");
            }
            priceRepository.deleteByProductId(Long.valueOf(productId));

        } catch(RestClientException ex){
            throw new RestClientException("Product with id " + id + " does not exist");
        } catch(ProductNotFoundException ex){
            throw new ProductNotFoundException(ex.getMessage());
        }
        return "Product with id " + stringId + " has been deleted";
    }

    // this method retrieves a valid product from Redsky Catalog and its associated pricing data
    // from our database and returns it to the client
    @Override
    public ProductInfo getById(Long id) throws ProductNotFoundException, RestClientException{

        ProductInfo productInfo = null;
        String stringId = String.valueOf(id);
        String url = String.format(config.getRedskyUrl(), stringId);

        try{
            ResponseEntity<CatalogProduct> response =  restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<CatalogProduct>() {
            });

            CatalogProduct redskyProduct = response.getBody();
            String productId = redskyProduct.getProduct().getItem().getProductId();
            Price priceInfo = priceRepository.findByProductId(Long.valueOf(productId));
            if(priceInfo == null){
                throw new ProductNotFoundException("Product with id " + stringId + " could not be found");
            }
            productInfo = getProductInfo(redskyProduct, priceInfo);

        } catch(RestClientException ex){
            throw new RestClientException("Product with id " + stringId + " is an invalid item");
        } catch (ProductNotFoundException ex){
            throw new ProductNotFoundException(ex.getMessage());
        }
        return productInfo;
    }

    // this method allows for a price update in our database of a verified Redsky Catalog product
    @Override
    public ProductInfo updatePrice(Long productId, ProductInfo productInfo) throws InvalidInputDataException, InvalidProductException, RestClientException {

        String stringId = String.valueOf(productId);
        String url = String.format(config.getRedskyUrl(), stringId);
        try{
            // an exception is thrown if the value of the productId in the path variable
            // differs from the one contained in the client request body
            if(!productId.equals(productInfo.getId())){
                throw new InvalidInputDataException("Inconsistent productId provided");
            }
            ResponseEntity<CatalogProduct> response =  restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<CatalogProduct>() {
            });
            CatalogProduct catalogProduct = response.getBody();
            Price dbPrice = priceRepository.findByProductId(Long.valueOf(catalogProduct.getProduct().getItem().getProductId()));
            if(dbPrice == null){
                throw new InvalidProductException("Product with id " + productId + " is not valid");
            }
            // the price provided by the client request body is formatted to a precision of 2 decimal places
            // before being saved to the database and returned in the client bound response object
            double updatedValue = Double.parseDouble(String.format("%.2f", productInfo.getCurrentPrice().getValue()));
            dbPrice.setValue(updatedValue);
            priceRepository.save(dbPrice);

            // the fields of the productInfo response object are reset by data from the call to catalog response,
            // price provided by the client, and our fixed enum value
            productInfo.getCurrentPrice().setValue(updatedValue);
            productInfo.getCurrentPrice().setCurrencyCode(Currency.USD.getValue());
            productInfo.setName(catalogProduct.getProduct().getItem().getProductDescription().getTitle());
            productInfo.setId(Long.valueOf(catalogProduct.getProduct().getItem().getProductId()));

        } catch(RestClientException ex){
            throw new RestClientException("Product with id " + productId + " does not exist");
        } catch(InvalidInputDataException e){
            throw new InvalidInputDataException(e.getMessage());
        } catch(InvalidProductException exc) {
            throw new InvalidProductException(exc.getMessage());
        }
        return productInfo;
    }

    // in this method, the fields of the controller bound response object (ProductInfo) are populated
    // with data for the call to Redsky Catalog response entity, price from our database, and our
    // fixed enum value (USD)
    private ProductInfo getProductInfo(CatalogProduct catalogProduct, Price priceInfo) {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(priceInfo.getProductId());
        productInfo.setName(catalogProduct.getProduct().getItem().getProductDescription().getTitle());
        productInfo.getCurrentPrice().setValue(Double.parseDouble(String.format("%.2f", priceInfo.getValue())));
        productInfo.getCurrentPrice().setCurrencyCode(Currency.USD.getValue());
        return  productInfo;
    }
}
