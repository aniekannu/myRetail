package com.aniekanudoessien.service;

import com.aniekanudoessien.exception.*;
import com.aniekanudoessien.model.*;
import com.aniekanudoessien.model.redskyproduct.CatalogProduct;
import com.aniekanudoessien.model.responseproduct.CurrentPrice;
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



    @Override
    public ProductInfo setPrice(PriceChange priceChange) throws RestClientException, ProductAlreadyExistsException {
        Price createPrice = new Price();
        ProductInfo createdProductInfo = null;
        String stringId = String.valueOf(priceChange.getProductId());
        String url = String.format(config.getRedskyUrl(), stringId);
        try{
            ResponseEntity<CatalogProduct> response =  restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<CatalogProduct>(){});
            CatalogProduct redskyProduct = response.getBody();
            String productId = redskyProduct.getProduct().getItem().getProductId();
            Price priceInfo = priceRepository.findByProductId(Long.valueOf(productId));
            if(priceInfo != null){
                throw new ProductAlreadyExistsException("Product with id " + stringId + " already exits");
            }
            createPrice.setProductId(Long.valueOf(redskyProduct.getProduct().getItem().getProductId()));
            createPrice.setValue(priceChange.getValue());
            priceRepository.save(createPrice);
            createdProductInfo = getProductInfo(redskyProduct, createPrice);
        } catch(RestClientException ex){
            throw new RestClientException("Product with id " + stringId + " is invalid");
        } catch(ProductAlreadyExistsException exc){
            throw new ProductAlreadyExistsException(exc.getMessage());
        }
        return createdProductInfo;
    }

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

    @Override
    public ProductInfo updatePrice(Long productId, ProductInfo productInfo) throws InvalidInputDataException, InvalidProductException, RestClientException {

        String stringId = String.valueOf(productId);
        String url = String.format(config.getRedskyUrl(), stringId);
        try{
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
            double updatedValue = Double.parseDouble(String.format("%,.2f", productInfo.getCurrentPrice().getValue()));
            dbPrice.setValue(updatedValue);
            priceRepository.save(dbPrice);
            productInfo.getCurrentPrice().setValue(updatedValue);

        } catch(RestClientException ex){
            throw new RestClientException("Product with id " + productId + " does not exist");
        } catch(InvalidInputDataException e){
            throw new InvalidInputDataException(e.getMessage());
        } catch(InvalidProductException exc) {
            throw new InvalidProductException(exc.getMessage());
        }
        return productInfo;
    }

    private ProductInfo getProductInfo(CatalogProduct catalogProduct, Price priceInfo) {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCurrentPrice(new CurrentPrice());
        productInfo.setId(priceInfo.getProductId());
        productInfo.setName(catalogProduct.getProduct().getItem().getProductDescription().getTitle());
        productInfo.getCurrentPrice().setValue(Double.parseDouble(String.format("%,.2f", priceInfo.getValue())));
        productInfo.getCurrentPrice().setCurrencyCode(Currency.USD.getValue());
        return  productInfo;
    }
}
