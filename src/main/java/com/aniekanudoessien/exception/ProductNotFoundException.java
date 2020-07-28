package com.aniekanudoessien.exception;

import org.springframework.web.client.RestClientException;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String msg) {
        super(msg);
    }
}
