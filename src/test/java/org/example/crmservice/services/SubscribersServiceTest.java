package org.example.crmservice.services;

import org.example.crmservice.clients.BRTServiceClient;
import org.example.crmservice.dtos.SubscriberDTO;
import org.example.crmservice.dtos.TopUpDTO;
import org.example.crmservice.dtos.fullSubscriberAndTariffInfo.FullSubscriberAndTariffInfoDTO;
import org.example.crmservice.dtos.fullSubscriberAndTariffInfo.SubscriberWithIdDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscribersServiceTest {

    @Mock
    private BRTServiceClient brtServiceClient;

    @InjectMocks
    private SubscribersService subscribersService;

    @Test
    void setTariffForSubscriber_delegatesToClient() {
        Long subscriberId = 1L;
        Long tariffId = 2L;
        String expectedResponse = "Tariff set successfully";
        
        when(brtServiceClient.setTariffForSubscriber(subscriberId, tariffId))
                .thenReturn(expectedResponse);

        String result = subscribersService.setTariffForSubscriber(subscriberId, tariffId);

        assertEquals(expectedResponse, result);
        verify(brtServiceClient).setTariffForSubscriber(subscriberId, tariffId);
    }

    @Test
    void getSubscriberInfo_delegatesToClient() {
        Long subscriberId = 1L;
        FullSubscriberAndTariffInfoDTO expectedDto = Mockito.mock(FullSubscriberAndTariffInfoDTO.class);
        
        when(brtServiceClient.getSubscriberAndTariffInfo(subscriberId))
                .thenReturn(expectedDto);

        FullSubscriberAndTariffInfoDTO result = subscribersService.getSubscriberInfo(subscriberId);

        assertEquals(expectedDto, result);
        verify(brtServiceClient).getSubscriberAndTariffInfo(subscriberId);
    }

    @Test
    void createSubscriber_delegatesToClient() {
        SubscriberDTO subscriberDTO = new SubscriberDTO("79001002030", "Test", "Middle", "User", 1L, new BigDecimal("100.00"));
        SubscriberWithIdDTO expectedDto = new SubscriberWithIdDTO(1L, "79001002030", "Test", "Middle", "User", 1L, new BigDecimal("100.00"));
        
        when(brtServiceClient.createSubscriber(subscriberDTO))
                .thenReturn(expectedDto);

        SubscriberWithIdDTO result = subscribersService.createSubscriber(subscriberDTO);

        assertEquals(expectedDto, result);
        verify(brtServiceClient).createSubscriber(subscriberDTO);
    }

    @Test
    void topUpBalance_delegatesToClient() {
        Long subscriberId = 1L;
        TopUpDTO topUpDTO = new TopUpDTO(new BigDecimal("100.00"), "y.e.");
        String expectedResponse = "Balance updated successfully";
        
        when(brtServiceClient.topUpBalance(subscriberId, topUpDTO))
                .thenReturn(expectedResponse);

        String result = subscribersService.topUpBalance(subscriberId, topUpDTO);

        assertEquals(expectedResponse, result);
        verify(brtServiceClient).topUpBalance(subscriberId, topUpDTO);
    }
}
