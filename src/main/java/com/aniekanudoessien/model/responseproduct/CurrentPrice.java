package com.aniekanudoessien.model.responseproduct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentPrice {

    @JsonProperty("value")
    private double value;

    @JsonProperty("currency_code")
    private String currencyCode;

    public CurrentPrice(){}

    public CurrentPrice(double value, String currencyCode) {
        this.value = value;
        this.currencyCode = currencyCode;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public String toString(){
        return "value = " + value + ", currency code = " + currencyCode + ".";
    }
}
