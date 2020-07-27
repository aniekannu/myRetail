package com.aniekanudoessien.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PriceChange {

    @JsonProperty("productId")
    private Long productId;
    @JsonProperty("value")
    private double value;

    public PriceChange(){}

    public PriceChange(Long productId, double value) {
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
