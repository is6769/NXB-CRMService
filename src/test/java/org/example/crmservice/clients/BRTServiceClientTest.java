package org.example.crmservice.clients;

import org.example.crmservice.dtos.SubscriberDTO;
import org.example.crmservice.dtos.TopUpDTO;
import org.example.crmservice.dtos.fullSubscriberAndTariffInfo.FullSubscriberAndTariffInfoDTO;
import org.example.crmservice.dtos.fullSubscriberAndTariffInfo.SubscriberWithIdDTO;
import org.example.crmservice.dtos.fullSubscriberAndTariffInfo.TariffDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Collections;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс для {@link BRTServiceClient}.
 * Проверяет, что клиент корректно формирует и отправляет запросы к BRT-сервису.
 */
@ExtendWith(MockitoExtension.class)
class BRTServiceClientTest {

    @Mock
    private RestClient.Builder restClientBuilder;

    @InjectMocks
    private BRTServiceClient brtServiceClient;

    private final String baseUrl = "http://BRT-service";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(brtServiceClient, "BASE_URL", baseUrl);
    }

    /**
     * Тестирует метод {@link BRTServiceClient#setTariffForSubscriber(Long, Long)}.
     * Проверяет, что формируется корректный PUT-запрос с правильным URI.
     */
    @Test
    void setTariffForSubscriber_shouldMakeProperRequest() {
        RestClient restClient = mock(RestClient.class);
        RestClient.RequestBodyUriSpec requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        
        when(restClientBuilder.build()).thenReturn(restClient);
        when(restClient.put()).thenReturn(requestBodyUriSpec);
        
        Long subscriberId = 1L;
        Long tariffId = 2L;
        String expectedResponse = "Tariff set successfully";
        
        ArgumentCaptor<Function<UriBuilder, URI>> uriCaptor = ArgumentCaptor.forClass(Function.class);
        
        when(requestBodyUriSpec.uri(eq(baseUrl), uriCaptor.capture())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn(expectedResponse);

        String result = brtServiceClient.setTariffForSubscriber(subscriberId, tariffId);

        assertEquals(expectedResponse, result);
        
        UriBuilder uriBuilder = UriComponentsBuilder.fromUriString(baseUrl);
        URI actualUri = uriCaptor.getValue().apply(uriBuilder);
        assertEquals(baseUrl + "/subscribers/1/tariff/2", actualUri.toString());
    }

    /**
     * Тестирует метод {@link BRTServiceClient#getSubscriberAndTariffInfo(Long)}.
     * Проверяет, что формируется корректный GET-запрос с правильным URI.
     */
    @Test
    void getSubscriberAndTariffInfo_shouldMakeProperRequest() {
        RestClient restClient = mock(RestClient.class);
        RestClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        
        when(restClientBuilder.build()).thenReturn(restClient);
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        
        Long subscriberId = 1L;
        FullSubscriberAndTariffInfoDTO expectedDto = new FullSubscriberAndTariffInfoDTO(
                new SubscriberWithIdDTO(1L, "79001234567", "Test", "Middle", "User", 1L, new BigDecimal("100.00")),
                new TariffDTO(1L, "Basic", "Basic Tariff", "30 days", true, Collections.emptyList())
        );
        
        ArgumentCaptor<Function<UriBuilder, URI>> uriCaptor = ArgumentCaptor.forClass(Function.class);
        
        when(requestHeadersUriSpec.uri(eq(baseUrl), uriCaptor.capture())).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(FullSubscriberAndTariffInfoDTO.class)).thenReturn(expectedDto);

        FullSubscriberAndTariffInfoDTO result = brtServiceClient.getSubscriberAndTariffInfo(subscriberId);

        assertEquals(expectedDto, result);
        
        UriBuilder uriBuilder = UriComponentsBuilder.fromUriString(baseUrl);
        URI actualUri = uriCaptor.getValue().apply(uriBuilder);
        assertEquals(baseUrl + "/subscribers/1", actualUri.toString());
    }

    /**
     * Тестирует метод {@link BRTServiceClient#createSubscriber(SubscriberDTO)}.
     * Проверяет, что формируется корректный POST-запрос с правильным URI и телом запроса.
     */
    @Test
    void createSubscriber_shouldMakeProperRequest() {
        RestClient restClient = mock(RestClient.class);
        RestClient.RequestBodyUriSpec requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        
        when(restClientBuilder.build()).thenReturn(restClient);
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        
        SubscriberDTO subscriberDTO = new SubscriberDTO(
                "79001234567", "Test", "Middle", "User", 1L, new BigDecimal("100.00"));
                
        SubscriberWithIdDTO expectedDto = new SubscriberWithIdDTO(
                1L, "79001234567", "Test", "Middle", "User", 1L, new BigDecimal("100.00"));
        
        ArgumentCaptor<Function<UriBuilder, URI>> uriCaptor = ArgumentCaptor.forClass(Function.class);
        
        when(requestBodyUriSpec.uri(eq(baseUrl), uriCaptor.capture())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(subscriberDTO)).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(SubscriberWithIdDTO.class)).thenReturn(expectedDto);

        SubscriberWithIdDTO result = brtServiceClient.createSubscriber(subscriberDTO);

        assertEquals(expectedDto, result);
        
        UriBuilder uriBuilder = UriComponentsBuilder.fromUriString(baseUrl);
        URI actualUri = uriCaptor.getValue().apply(uriBuilder);
        assertEquals(baseUrl + "/subscriber", actualUri.toString());
        
        verify(requestBodySpec).body(subscriberDTO);
    }

    /**
     * Тестирует метод {@link BRTServiceClient#topUpBalance(Long, TopUpDTO)}.
     * Проверяет, что формируется корректный PATCH-запрос с правильным URI и телом запроса.
     */
    @Test
    void topUpBalance_shouldMakeProperRequest() {
        RestClient restClient = mock(RestClient.class);
        RestClient.RequestBodyUriSpec requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        
        when(restClientBuilder.build()).thenReturn(restClient);
        when(restClient.patch()).thenReturn(requestBodyUriSpec);
        
        Long subscriberId = 1L;
        TopUpDTO topUpDTO = new TopUpDTO(new BigDecimal("100.00"), "y.e.");
        String expectedResponse = "Balance updated successfully";
        
        ArgumentCaptor<Function<UriBuilder, URI>> uriCaptor = ArgumentCaptor.forClass(Function.class);
        
        when(requestBodyUriSpec.uri(eq(baseUrl), uriCaptor.capture())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(topUpDTO)).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn(expectedResponse);

        String result = brtServiceClient.topUpBalance(subscriberId, topUpDTO);

        assertEquals(expectedResponse, result);
        
        UriBuilder uriBuilder = UriComponentsBuilder.fromUriString(baseUrl);
        URI actualUri = uriCaptor.getValue().apply(uriBuilder);
        assertEquals(baseUrl + "/subscribers/1/balance", actualUri.toString());
        
        verify(requestBodySpec).body(topUpDTO);
    }
}
