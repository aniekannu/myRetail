package com.aniekanudoessien.repository;

import com.aniekanudoessien.model.Price;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PriceRepository extends MongoRepository<Price, Integer> {

    Price findByProductId(String productId);
}