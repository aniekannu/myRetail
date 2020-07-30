package com.aniekanudoessien.repository;

import com.aniekanudoessien.model.dbdocument.Price;
import org.springframework.data.mongodb.repository.MongoRepository;

// custom repository interface that's used to perform CRUD operations on the database
public interface PriceRepository extends MongoRepository<Price, String> {

    // 2 custom defined methods to fetch or delete
    // documents from the database with respect to productId
    Price findByProductId(Long id);

    void deleteByProductId(Long id);
}
