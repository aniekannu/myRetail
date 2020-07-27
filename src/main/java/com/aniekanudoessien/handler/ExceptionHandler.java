package com.aniekanudoessien.handler;

import com.aniekanudoessien.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;


@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResonse> handleProductNotFound(ProductNotFoundException ex){
        ErrorResonse error = new ErrorResonse();
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResonse> handleProductNotFound(Exception ex){
        ErrorResonse error = new ErrorResonse();
        error.setMessage("Operation could not be performed");
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
