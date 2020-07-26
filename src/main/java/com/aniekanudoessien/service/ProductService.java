package com.aniekanudoessien.service;

import com.aniekanudoessien.model.ProductInfo;

public interface ProductService {

    ProductInfo getById(String id);

    ProductInfo update(ProductInfo productInfo);
}
