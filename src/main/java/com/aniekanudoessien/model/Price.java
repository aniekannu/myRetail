package com.aniekanudoessien.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "price")
public class Price {

    @Id
    private int id;
    private String productId;
    private int value;

    public Price(){}

    public Price(String productId, int value) {
        this.productId = productId;
        this.value = value;
    }
}
