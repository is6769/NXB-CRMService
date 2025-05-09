package org.example.crmservice.exceptions.handlers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;


@RestControllerAdvice
public class RestExceptionsHandler {
    @ExceptionHandler(exception = {HttpClientErrorException.class, HttpServerErrorException.class})
    public ResponseEntity<byte[]> handleHttpClientExceptions(HttpStatusCodeException ex){
        return ResponseEntity
                .status(ex.getStatusCode())
                .headers(headers -> headers.addAll(ex.getResponseHeaders()))
                .body(ex.getResponseBodyAsByteArray());
    }
}
