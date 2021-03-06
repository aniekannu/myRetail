package com.aniekanudoessien.util;

public class MyRetailPath {

    // constants that represent Http request paths to various endpoints in the application

    public static final String MYRETAIL_BASE_PATH = "/myretail/v1";
    public static final String RESOURCE_PATH_VARIABLE = "/products/{productId}";
    public static final String RESOURCE_PRICES = "/products/prices";
    public static final String CREATE_RESOURCE_PRICES = MYRETAIL_BASE_PATH + RESOURCE_PRICES;
}
