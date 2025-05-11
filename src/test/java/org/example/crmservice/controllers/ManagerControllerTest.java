package org.example.crmservice.controllers;

import org.example.crmservice.dtos.SubscriberDTO;
import org.example.crmservice.dtos.TopUpDTO;
import org.example.crmservice.dtos.fullSubscriberAndTariffInfo.FullSubscriberAndTariffInfoDTO;
import org.example.crmservice.dtos.fullSubscriberAndTariffInfo.SubscriberWithIdDTO;
import org.example.crmservice.dtos.fullSubscriberAndTariffInfo.TariffDTO;
import org.example.crmservice.exceptions.handlers.RestExceptionsHandler;
import org.example.crmservice.services.SubscribersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ManagerControllerTest {

    @Mock
    private SubscribersService subscribersService;

    @InjectMocks
    private ManagerController managerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(managerController)
                .setControllerAdvice(new RestExceptionsHandler())
                .build();
    }

    @Test
    void createSubscriber_withValidData_returnsCreatedSubscriber() throws Exception {
        SubscriberDTO subscriberDTO = new SubscriberDTO("79001234567", "Test", "Middle", "User", 1L, new BigDecimal("100.00"));
        SubscriberWithIdDTO response = new SubscriberWithIdDTO(1L, "79001234567", "Test", "Middle", "User", 1L, new BigDecimal("100.00"));
        
        when(subscribersService.createSubscriber(any(SubscriberDTO.class))).thenReturn(response);
        
        mockMvc.perform(post("/manager/subscriber")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"msisdn\":\"79001234567\",\"firstName\":\"Test\",\"secondName\":\"Middle\",\"surname\":\"User\",\"tariffId\":1,\"balance\":100.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.msisdn").value("79001234567"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.secondName").value("Middle"))
                .andExpect(jsonPath("$.surname").value("User"))
                .andExpect(jsonPath("$.tariffId").value(1))
                .andExpect(jsonPath("$.balance").value(100.00));
        
        verify(subscribersService).createSubscriber(any(SubscriberDTO.class));
    }

    @Test
    void createSubscriber_withInvalidMsisdn_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/manager/subscriber")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"msisdn\":\"invalid-msisdn\",\"firstName\":\"Test\",\"secondName\":\"Middle\",\"surname\":\"User\",\"tariffId\":1,\"balance\":100.00}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    assert exception instanceof MethodArgumentNotValidException;
                });
        
        verifyNoInteractions(subscribersService);
    }

    @Test
    void createSubscriber_withMissingRequiredFields_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/manager/subscriber")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"msisdn\":\"79001234567\",\"secondName\":\"Middle\",\"tariffId\":1,\"balance\":100.00}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    assert exception instanceof MethodArgumentNotValidException;
                });
        
        verifyNoInteractions(subscribersService);
    }

    @Test
    void createSubscriber_withNegativeTariffId_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/manager/subscriber")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"msisdn\":\"79001234567\",\"firstName\":\"Test\",\"secondName\":\"Middle\",\"surname\":\"User\",\"tariffId\":-1,\"balance\":100.00}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    assert exception instanceof MethodArgumentNotValidException;
                });
        
        verifyNoInteractions(subscribersService);
    }

    @Test
    void createSubscriber_withNegativeBalance_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/manager/subscriber")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"msisdn\":\"79001234567\",\"firstName\":\"Test\",\"secondName\":\"Middle\",\"surname\":\"User\",\"tariffId\":1,\"balance\":-100.00}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    assert exception instanceof MethodArgumentNotValidException;
                });
        
        verifyNoInteractions(subscribersService);
    }

    @Test
    void setTariffForSubscriber_withValidData_returnsSuccessMessage() throws Exception {
        Long subscriberId = 1L;
        Long tariffId = 2L;
        String response = "Tariff set successfully";
        
        when(subscribersService.setTariffForSubscriber(subscriberId, tariffId)).thenReturn(response);
        
        mockMvc.perform(put("/manager/subscribers/1/tariff/2"))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
        
        verify(subscribersService).setTariffForSubscriber(subscriberId, tariffId);
    }

    @Test
    void topUpBalance_withValidData_returnsSuccessMessage() throws Exception {
        Long subscriberId = 1L;
        TopUpDTO topUpDTO = new TopUpDTO(new BigDecimal("100.00"), "y.e.");
        String response = "Balance updated successfully";
        
        when(subscribersService.topUpBalance(eq(subscriberId), any(TopUpDTO.class))).thenReturn(response);
        
        mockMvc.perform(patch("/manager/subscribers/1/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":100.00,\"unit\":\"y.e.\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
        
        verify(subscribersService).topUpBalance(eq(subscriberId), any(TopUpDTO.class));
    }

    @Test
    void topUpBalance_withNegativeAmount_returnsBadRequest() throws Exception {
        mockMvc.perform(patch("/manager/subscribers/1/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":-100.00,\"unit\":\"y.e.\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    assert exception instanceof MethodArgumentNotValidException;
                });
        
        verifyNoInteractions(subscribersService);
    }

    @Test
    void topUpBalance_withZeroAmount_returnsBadRequest() throws Exception {
        mockMvc.perform(patch("/manager/subscribers/1/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":0.00,\"unit\":\"y.e.\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    assert exception instanceof MethodArgumentNotValidException;
                });
        
        verifyNoInteractions(subscribersService);
    }

    @Test
    void topUpBalance_withTooSmallAmount_returnsBadRequest() throws Exception {
        mockMvc.perform(patch("/manager/subscribers/1/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":0.05,\"unit\":\"y.e.\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    assert exception instanceof MethodArgumentNotValidException;
                });
        
        verifyNoInteractions(subscribersService);
    }

    @Test
    void topUpBalance_withMissingUnit_returnsBadRequest() throws Exception {
        mockMvc.perform(patch("/manager/subscribers/1/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":100.00}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    assert exception instanceof MethodArgumentNotValidException;
                });
        
        verifyNoInteractions(subscribersService);
    }

    @Test
    void topUpBalance_withEmptyUnit_returnsBadRequest() throws Exception {
        mockMvc.perform(patch("/manager/subscribers/1/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":100.00,\"unit\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    assert exception instanceof MethodArgumentNotValidException;
                });
        
        verifyNoInteractions(subscribersService);
    }

    @Test
    void getSubscriberAndTariffInfo_withValidData_returnsSubscriberWithTariff() throws Exception {
        Long subscriberId = 1L;
        FullSubscriberAndTariffInfoDTO response = new FullSubscriberAndTariffInfoDTO(
            new SubscriberWithIdDTO(1L, "79001234567", "Test", "Middle", "User", 1L, new BigDecimal("100.00")),
            new TariffDTO(1L, "Basic", "Basic Tariff", "30 days", true, Collections.emptyList())
        );
        
        when(subscribersService.getSubscriberInfo(subscriberId)).thenReturn(response);
        
        mockMvc.perform(get("/manager/subscribers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subscriber.id").value(1))
                .andExpect(jsonPath("$.subscriber.msisdn").value("79001234567"))
                .andExpect(jsonPath("$.subscriber.firstName").value("Test"))
                .andExpect(jsonPath("$.subscriber.secondName").value("Middle"))
                .andExpect(jsonPath("$.subscriber.surname").value("User"))
                .andExpect(jsonPath("$.subscriber.tariffId").value(1))
                .andExpect(jsonPath("$.subscriber.balance").value(100.0))
                .andExpect(jsonPath("$.tariff.id").value(1))
                .andExpect(jsonPath("$.tariff.name").value("Basic"))
                .andExpect(jsonPath("$.tariff.description").value("Basic Tariff"))
                .andExpect(jsonPath("$.tariff.cycleSize").value("30 days"))
                .andExpect(jsonPath("$.tariff.is_active").value(true))
                .andExpect(jsonPath("$.tariff.tariffPackages").isArray());
        
        verify(subscribersService).getSubscriberInfo(subscriberId);
    }
}
