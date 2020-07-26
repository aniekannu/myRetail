package com.aniekanudoessien.exception;

import org.springframework.web.client.RestClientException;

public class ProductNotFoundException extends RestClientException {

    public ProductNotFoundException(String msg) {
        super(msg);
    }
}
