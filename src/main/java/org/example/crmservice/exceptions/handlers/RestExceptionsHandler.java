package org.example.crmservice.exceptions.handlers;


import jakarta.servlet.http.HttpServletRequest;
import org.example.crmservice.dtos.ExceptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Map;


@RestControllerAdvice
public class RestExceptionsHandler {

    @ExceptionHandler(exception = {HttpClientErrorException.class, HttpServerErrorException.class})
    public ResponseEntity<byte[]> handleHttpClientExceptions(HttpStatusCodeException ex){
        HttpHeaders originalHeaders = ex.getResponseHeaders();
        HttpHeaders newHeaders = new HttpHeaders();
        if (originalHeaders != null) {
            originalHeaders.forEach((key, value) -> {
                if (!key.equalsIgnoreCase(HttpHeaders.TRANSFER_ENCODING) &&
                        !key.equalsIgnoreCase(HttpHeaders.CONTENT_LENGTH)) {
                    newHeaders.put(key, value);
                }
            });
            if (originalHeaders.getContentType() != null) {
                newHeaders.setContentType(originalHeaders.getContentType());
            }
        }

        return ResponseEntity
                .status(ex.getStatusCode())
                .headers(newHeaders)
                .body(ex.getResponseBodyAsByteArray());
    }

    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handleSubscriberAlreadyExistsException(HttpServletRequest request, Exception ex){
        return new ExceptionDTO(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_EXCEPTION",
                ex.getMessage(),
                request.getRequestURL().toString()
        );
    }
}
