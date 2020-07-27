package com.aniekanudoessien.exception;

import org.springframework.web.client.RestClientException;

public class InvalidProductException extends RestClientException {

    public InvalidProductException(String msg) {
        super(msg);
    }
}
