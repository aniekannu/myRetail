package com.aniekanudoessien.service;

import com.aniekanudoessien.model.PriceChange;
import com.aniekanudoessien.model.responseproduct.ProductInfo;

public interface ProductService {

    ProductInfo getById(Long id) throws Exception;

    ProductInfo setPrice (PriceChange priceChange) throws Exception;

    ProductInfo updatePrice(Long productId, ProductInfo productInfo) throws Exception;
}
