package org.example.crmservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.crmservice.dtos.SubscriberDTO;
import org.example.crmservice.dtos.TopUpDTO;
import org.example.crmservice.dtos.fullSubscriberAndTariffInfo.FullSubscriberAndTariffInfoDTO;
import org.example.crmservice.dtos.fullSubscriberAndTariffInfo.SubscriberWithIdDTO;
import org.example.crmservice.dtos.fullSubscriberAndTariffInfo.TariffDTO;
import org.example.crmservice.exceptions.handlers.RestExceptionsHandler;
import org.example.crmservice.services.SubscribersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(managerController)
                .setControllerAdvice(new RestExceptionsHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("Subscriber Creation Tests")
    class SubscriberCreationTests {
        
        @Test
        @DisplayName("createSubscriber with valid data returns created subscriber")
        void createSubscriber_withValidData_returnsCreatedSubscriber() throws Exception {
            SubscriberDTO subscriberDTO = new SubscriberDTO("79001234567", "Test", "Middle", "User", 1L, new BigDecimal("100.00"));
            SubscriberWithIdDTO response = new SubscriberWithIdDTO(1L, "79001234567", "Test", "Middle", "User", 1L, new BigDecimal("100.00"));
            
            when(subscribersService.createSubscriber(any(SubscriberDTO.class))).thenReturn(response);
            
            mockMvc.perform(post("/manager/subscriber")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(subscriberDTO)))
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
        @DisplayName("createSubscriber with null optional fields is valid")
        void createSubscriber_withNullOptionalFields_isValid() throws Exception {
            SubscriberDTO subscriberDTO = new SubscriberDTO("79001234567", "Test", null, "User", null, null);
            SubscriberWithIdDTO response = new SubscriberWithIdDTO(1L, "79001234567", "Test", null, "User", null, null);
            
            when(subscribersService.createSubscriber(any(SubscriberDTO.class))).thenReturn(response);
            
            mockMvc.perform(post("/manager/subscriber")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(subscriberDTO)))
                    .andExpect(status().isOk());
            
            verify(subscribersService).createSubscriber(any(SubscriberDTO.class));
        }
        
        @ParameterizedTest
        @DisplayName("createSubscriber with invalid MSISDN pattern returns BadRequest")
        @ValueSource(strings = {"msisdn", "123-456-789", "abcdefg", "", " "})
        void createSubscriber_withInvalidMsisdn_returnsBadRequest(String invalidMsisdn) throws Exception {
            Map<String, Object> subscriberMap = new HashMap<>();
            subscriberMap.put("msisdn", invalidMsisdn);
            subscriberMap.put("firstName", "Test");
            subscriberMap.put("surname", "User");
            
            mockMvc.perform(post("/manager/subscriber")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(subscriberMap)))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> {
                        Exception exception = result.getResolvedException();
                        assert exception instanceof MethodArgumentNotValidException;
                    });
            
            verifyNoInteractions(subscribersService);
        }
        
        @Test
        @DisplayName("createSubscriber with missing required firstName returns BadRequest")
        void createSubscriber_withMissingFirstName_returnsBadRequest() throws Exception {
            Map<String, Object> subscriberMap = new HashMap<>();
            subscriberMap.put("msisdn", "79001234567");
            subscriberMap.put("surname", "User");
            
            mockMvc.perform(post("/manager/subscriber")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(subscriberMap)))
                    .andExpect(status().isBadRequest());
            
            verifyNoInteractions(subscribersService);
        }
        
        @Test
        @DisplayName("createSubscriber with missing required surname returns BadRequest")
        void createSubscriber_withMissingSurname_returnsBadRequest() throws Exception {
            Map<String, Object> subscriberMap = new HashMap<>();
            subscriberMap.put("msisdn", "79001234567");
            subscriberMap.put("firstName", "Test");
            
            mockMvc.perform(post("/manager/subscriber")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(subscriberMap)))
                    .andExpect(status().isBadRequest());
            
            verifyNoInteractions(subscribersService);
        }

        @Test
        @DisplayName("createSubscriber with negative tariffId returns BadRequest")
        void createSubscriber_withNegativeTariffId_returnsBadRequest() throws Exception {
            Map<String, Object> subscriberMap = new HashMap<>();
            subscriberMap.put("msisdn", "79001234567");
            subscriberMap.put("firstName", "Test");
            subscriberMap.put("surname", "User");
            subscriberMap.put("tariffId", -1);
            
            mockMvc.perform(post("/manager/subscriber")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(subscriberMap)))
                    .andExpect(status().isBadRequest());
            
            verifyNoInteractions(subscribersService);
        }

        @Test
        @DisplayName("createSubscriber with negative balance returns BadRequest")
        void createSubscriber_withNegativeBalance_returnsBadRequest() throws Exception {
            Map<String, Object> subscriberMap = new HashMap<>();
            subscriberMap.put("msisdn", "79001234567");
            subscriberMap.put("firstName", "Test");
            subscriberMap.put("surname", "User");
            subscriberMap.put("balance", -100.00);
            
            mockMvc.perform(post("/manager/subscriber")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(subscriberMap)))
                    .andExpect(status().isBadRequest());
            
            verifyNoInteractions(subscribersService);
        }
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
                .content(objectMapper.writeValueAsString(topUpDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
        
        verify(subscribersService).topUpBalance(eq(subscriberId), any(TopUpDTO.class));
    }

    @Test
    void topUpBalance_withNegativeAmount_returnsBadRequest() throws Exception {
        TopUpDTO topUpDTO = new TopUpDTO(new BigDecimal("-100.00"), "y.e.");
        
        mockMvc.perform(patch("/manager/subscribers/1/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(topUpDTO)))
                .andExpect(status().isBadRequest());
        
        verifyNoInteractions(subscribersService);
    }

    @Test
    void topUpBalance_withZeroAmount_returnsBadRequest() throws Exception {
        TopUpDTO topUpDTO = new TopUpDTO(new BigDecimal("0.00"), "y.e.");
        
        mockMvc.perform(patch("/manager/subscribers/1/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(topUpDTO)))
                .andExpect(status().isBadRequest());
        
        verifyNoInteractions(subscribersService);
    }

    @Test
    void topUpBalance_withTooSmallAmount_returnsBadRequest() throws Exception {
        TopUpDTO topUpDTO = new TopUpDTO(new BigDecimal("0.05"), "y.e.");
        
        mockMvc.perform(patch("/manager/subscribers/1/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(topUpDTO)))
                .andExpect(status().isBadRequest());
        
        verifyNoInteractions(subscribersService);
    }

    @Test
    void topUpBalance_withMissingUnit_returnsBadRequest() throws Exception {
        Map<String, Object> topUpMap = new HashMap<>();
        topUpMap.put("amount", 100.00);
        
        mockMvc.perform(patch("/manager/subscribers/1/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(topUpMap)))
                .andExpect(status().isBadRequest());
        
        verifyNoInteractions(subscribersService);
    }

    @Test
    void topUpBalance_withEmptyUnit_returnsBadRequest() throws Exception {
        TopUpDTO topUpDTO = new TopUpDTO(new BigDecimal("100.00"), "");
        
        mockMvc.perform(patch("/manager/subscribers/1/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(topUpDTO)))
                .andExpect(status().isBadRequest());
        
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
