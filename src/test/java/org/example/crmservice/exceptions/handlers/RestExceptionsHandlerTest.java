package org.example.crmservice.exceptions.handlers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.crmservice.dtos.ExceptionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class RestExceptionsHandlerTest {

    @Mock
    private HttpServletRequest mockRequest;

    @InjectMocks
    private RestExceptionsHandler restExceptionsHandler;

    private HttpHeaders originalHeaders;

    @BeforeEach
    void setUp() {
        originalHeaders = new HttpHeaders();
        originalHeaders.setContentType(MediaType.APPLICATION_JSON);
        originalHeaders.setContentLength(100);
        originalHeaders.set("X-Custom-Header", "CustomValue");
        originalHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
    }

    @Test
    @DisplayName("handleHttpClientExceptions should correctly propagate HttpClientErrorException")
    void handleHttpClientExceptions_withHttpClientErrorException() {
        byte[] responseBody = "{\"error\":\"client error\"}".getBytes(StandardCharsets.UTF_8);
        HttpClientErrorException ex = new HttpClientErrorException(
                HttpStatus.BAD_REQUEST,
                "Client Error",
                originalHeaders,
                responseBody,
                StandardCharsets.UTF_8
        );

        ResponseEntity<byte[]> responseEntity = restExceptionsHandler.handleHttpClientExceptions(ex);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo(responseBody);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(responseEntity.getHeaders().getFirst("X-Custom-Header")).isEqualTo("CustomValue");
        assertThat(responseEntity.getHeaders().containsKey(HttpHeaders.CONTENT_LENGTH)).isFalse();
        assertThat(responseEntity.getHeaders().containsKey(HttpHeaders.TRANSFER_ENCODING)).isFalse();
    }

    @Test
    @DisplayName("handleHttpClientExceptions should correctly propagate HttpServerErrorException")
    void handleHttpClientExceptions_withHttpServerErrorException() {
        byte[] responseBody = "{\"error\":\"server error\"}".getBytes(StandardCharsets.UTF_8);
        HttpServerErrorException ex = new HttpServerErrorException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Server Error",
                originalHeaders,
                responseBody,
                StandardCharsets.UTF_8
        );

        ResponseEntity<byte[]> responseEntity = restExceptionsHandler.handleHttpClientExceptions(ex);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getBody()).isEqualTo(responseBody);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(responseEntity.getHeaders().getFirst("X-Custom-Header")).isEqualTo("CustomValue");
        assertThat(responseEntity.getHeaders().containsKey(HttpHeaders.CONTENT_LENGTH)).isFalse();
        assertThat(responseEntity.getHeaders().containsKey(HttpHeaders.TRANSFER_ENCODING)).isFalse();
    }
    
    @Test
    @DisplayName("handleHttpClientExceptions should handle null original headers gracefully")
    void handleHttpClientExceptions_withNullOriginalHeaders() {
        byte[] responseBody = "{\"error\":\"client error\"}".getBytes(StandardCharsets.UTF_8);
        HttpClientErrorException ex = new HttpClientErrorException(
                HttpStatus.NOT_FOUND,
                "Not Found",
                null, 
                responseBody,
                StandardCharsets.UTF_8
        );

        ResponseEntity<byte[]> responseEntity = restExceptionsHandler.handleHttpClientExceptions(ex);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo(responseBody);
        assertThat(responseEntity.getHeaders()).isNotNull();
        assertThat(responseEntity.getHeaders().isEmpty()).isTrue(); 
    }


    @Test
    @DisplayName("handleSubscriberAlreadyExistsException should correctly handle MethodArgumentNotValidException")
    void handleSubscriberAlreadyExistsException_withMethodArgumentNotValidException() {
        String errorMessage = "Validation failed for argument";
        String requestUrl = "http://localhost/api/test";

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getMessage()).thenReturn(errorMessage);
        when(mockRequest.getRequestURL()).thenReturn(new StringBuffer(requestUrl));

        ExceptionDTO exceptionDTO = restExceptionsHandler.handleSubscriberAlreadyExistsException(mockRequest, ex);

        assertThat(exceptionDTO.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionDTO.exceptionType()).isEqualTo("VALIDATION_EXCEPTION");
        assertThat(exceptionDTO.message()).isEqualTo(errorMessage);
        assertThat(exceptionDTO.url()).isEqualTo(requestUrl);
    }
}
