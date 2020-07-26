package com.aniekanudoessien.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentPrice {

    @JsonProperty("value")
    private int value;
    @JsonProperty("currency_code")
    private String currencyCode;

    public CurrentPrice(){}

    public CurrentPrice(int value, String currencyCode) {
        this.value = value;
        this.currencyCode = currencyCode;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
