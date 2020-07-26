package com.aniekanudoessien.service;

import com.aniekanudoessien.exception.ProductNotFoundException;
import com.aniekanudoessien.model.Currency;
import com.aniekanudoessien.model.Price;
import com.aniekanudoessien.model.ProductDetail;
import com.aniekanudoessien.model.ProductInfo;
import com.aniekanudoessien.repository.PriceRepository;
import com.aniekanudoessien.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
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
    public ProductInfo getById(String id) throws ProductNotFoundException {

        ProductInfo productInfo = null;
        String url = String.format(config.getRedskyUrl(), id);

        try{
            ResponseEntity<ProductDetail> response =  restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ProductDetail>() {
            });

            ProductDetail productDetail = response.getBody();
            Price priceInfo = priceRepository.findByProductId(productDetail.getProduct().getItem().getProductId());
            if(ObjectUtils.isEmpty(priceInfo) || ObjectUtils.isEmpty(priceInfo)){
                throw new ProductNotFoundException("");
            }

            productInfo = getProductInfo(productDetail, priceInfo);

        } catch (ProductNotFoundException ex){

        }
        return productInfo;
    }

    @Override
    public ProductInfo update(ProductInfo productInfo) {
        return null;
    }

    private ProductInfo getProductInfo(ProductDetail productDetail, Price priceInfo){
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(priceInfo.getProductId());
        productInfo.setName(productDetail.getProduct().getItem().getProductDescription().getTitle());
        productInfo.getCurrentPrice().setValue(priceInfo.getValue());
        productInfo.getCurrentPrice().setCurrencyCode(Currency.USD.name());
        return  productInfo;
    }
}
