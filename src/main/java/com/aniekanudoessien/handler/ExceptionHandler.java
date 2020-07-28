package com.aniekanudoessien.handler;

import com.aniekanudoessien.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.RestClientException;


@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResonse> handleProductNotFound(ProductNotFoundException ex){
        ErrorResonse error = new ErrorResonse();
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ErrorResonse> handleExistingProduct(ProductAlreadyExistsException ex){
        ErrorResonse error = new ErrorResonse();
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidInputDataException.class)
    public ResponseEntity<ErrorResonse> handleInvalidInputData(InvalidInputDataException ex) {
        ErrorResonse error = new ErrorResonse();
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(CreatedProductException.class)
    public ResponseEntity<ErrorResonse> productCreationException(CreatedProductException ex) {
        ErrorResonse error = new ErrorResonse();
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<ErrorResonse> handleInvalidProduct(InvalidProductException ex){
        ErrorResonse error = new ErrorResonse();
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(RestClientException.class)
    public ResponseEntity<ErrorResonse> handleInvalidProduct(RestClientException ex){
        ErrorResonse error = new ErrorResonse();
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResonse> handleProductNotFound(Exception ex){
        ErrorResonse error = new ErrorResonse();
        error.setMessage("Operation could not be performed");
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
