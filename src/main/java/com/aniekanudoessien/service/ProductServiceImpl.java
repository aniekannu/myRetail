package com.aniekanudoessien.service;

import com.aniekanudoessien.exception.ProductNotFoundException;
import com.aniekanudoessien.model.*;
import com.aniekanudoessien.model.redskyproduct.ProductDetail;
import com.aniekanudoessien.model.responseproduct.CurrentPrice;
import com.aniekanudoessien.model.responseproduct.ProductInfo;
import com.aniekanudoessien.repository.PriceRepository;
import com.aniekanudoessien.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
    public ProductInfo getById(Long id) {

        ProductInfo productInfo = null;
        String stringId = String.valueOf(id);
        String url = String.format(config.getRedskyUrl(), stringId);

        try{
            ResponseEntity<ProductDetail> response =  restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ProductDetail>() {
            });

            ProductDetail productDetail = response.getBody();
            String productId = productDetail.getProduct().getItem().getProductId();
            Price priceInfo = priceRepository.findByProductId(Long.valueOf(productId));
            productInfo = getProductInfo(productDetail, priceInfo);

        } catch (ProductNotFoundException ex){
            throw new ProductNotFoundException("Product with id " + stringId + " could not be found");
        }
        return productInfo;
    }

    @Override
    public ProductInfo updatePrice(Long productId, ProductInfo productInfo) {

        String stringId = String.valueOf(productId);
        String url = String.format(config.getRedskyUrl(), stringId);
        try{
            ResponseEntity<ProductDetail> response =  restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ProductDetail>() {
            });
            ProductDetail productDetail = response.getBody();
            Price dbPrice = priceRepository.findByProductId(Long.valueOf(productDetail.getProduct().getItem().getProductId()));
            dbPrice.setValue(productInfo.getCurrentPrice().getValue());
            priceRepository.save(dbPrice);

        } catch(ProductNotFoundException ex){
            throw new ProductNotFoundException("The price of product with id " + stringId + " could not be updated");
        }
        return productInfo;
    }

    @Override
    public ProductInfo setPrice(PriceChange priceChange) {
        Price createPrice = new Price();
        ProductInfo createdProductInfo = null;
        String stringId = String.valueOf(priceChange.getProductId());
        String url = String.format(config.getRedskyUrl(), stringId);
        try{
            ResponseEntity<ProductDetail> response =  restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ProductDetail>(){});
            ProductDetail redskyProduct = response.getBody();
            createPrice.setProductId(Long.valueOf(redskyProduct.getProduct().getItem().getProductId()));
            createPrice.setValue(priceChange.getValue());
            priceRepository.save(createPrice);
            createdProductInfo = getProductInfo(redskyProduct, createPrice);

        } catch(ProductNotFoundException ex){
            throw new ProductNotFoundException("Product with id " + stringId + " is not a valid product");
        }
        return createdProductInfo;
    }

    @Override
    public String deleteProduct(Long id) {
        ProductInfo productInfo = null;
        String stringId = String.valueOf(id);
        String url = String.format(config.getRedskyUrl(), stringId);

        try{
            ResponseEntity<ProductDetail> response =  restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ProductDetail>() {
            });

            ProductDetail productDetail = response.getBody();
            String productId = productDetail.getProduct().getItem().getProductId();
            Price priceInfo = priceRepository.findByProductId(Long.valueOf(productId));
            if(priceInfo == null){
                throw new ProductNotFoundException("Product with id " + stringId + " could not be found");
            }
            priceRepository.deleteByProductId(Long.valueOf(productId));

        } catch(Exception ex){
            throw new ProductNotFoundException("Product with id " + stringId + " could not be found");
        }
        return "Product with id " + stringId + " has been deleted";
    }

    private ProductInfo getProductInfo(ProductDetail productDetail, Price priceInfo) {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCurrentPrice(new CurrentPrice());
        productInfo.setId(priceInfo.getProductId());
        productInfo.setName(productDetail.getProduct().getItem().getProductDescription().getTitle());
        productInfo.getCurrentPrice().setValue(priceInfo.getValue());
        productInfo.getCurrentPrice().setCurrencyCode(Currency.USD.getValue());
        return  productInfo;
    }
}
