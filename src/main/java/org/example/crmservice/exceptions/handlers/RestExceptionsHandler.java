package org.example.crmservice.exceptions.handlers;


import jakarta.servlet.http.HttpServletRequest;
import org.example.crmservice.dtos.ExceptionDTO;
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

/**
 * Глобальный обработчик исключений для REST контроллеров.
 * Перехватывает специфичные исключения и возвращает стандартизированные HTTP ответы.
 */
@RestControllerAdvice
public class RestExceptionsHandler {

    /**
     * Обрабатывает исключения типа {@link HttpClientErrorException}, возникающие при клиентских ошибках (4xx).
     * Возвращает ответ с тем же HTTP статусом, телом и заголовками, что и в исходном исключении.
     * @param ex Исключение {@link HttpClientErrorException}.
     * @return {@link ResponseEntity} с деталями ошибки.
     */
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

    /**
     * Обрабатывает исключения типа {@link MethodArgumentNotValidException}, возникающие при ошибках валидации аргументов метода.
     * Возвращает ответ со статусом BAD_REQUEST и {@link ExceptionDTO}, содержащим информацию об ошибках валидации.
     * @param ex Исключение {@link MethodArgumentNotValidException}.
     * @return {@link ResponseEntity} с {@link ExceptionDTO}, описывающим ошибки валидации.
     */
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
