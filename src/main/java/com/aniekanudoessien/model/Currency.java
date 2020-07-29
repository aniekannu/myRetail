package com.aniekanudoessien.model;

public enum Currency {
    USD("USD");

    private String value;

    public String getValue(){
        return value;
    }

    Currency(String value) {
        this.value = value;
    }
}
