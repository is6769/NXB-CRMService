package org.example.crmservice.dtos;

public record ExceptionDTO(
        Integer status,
        String exceptionType,
        String message,
        String url
) {
}
