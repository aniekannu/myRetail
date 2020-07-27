package com.aniekanudoessien.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "price")
public class Price {

    @Id
    private String id;
    private Long productId;
    private double value;

    public Price(){}

    public Price(Long productId, double value) {
        this.productId = productId;
        this.value = value;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
