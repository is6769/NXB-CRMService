package org.example.crmservice.services;

import org.example.crmservice.clients.BRTServiceClient;
import org.example.crmservice.dtos.SubscriberDTO;
import org.example.crmservice.dtos.TopUpDTO;
import org.example.crmservice.dtos.fullSubscriberAndTariffInfo.FullSubscriberAndTariffInfoDTO;
import org.springframework.stereotype.Service;

@Service
public class SubscribersService {

    private final BRTServiceClient brtServiceClient;

    public SubscribersService(BRTServiceClient brtServiceClient) {
        this.brtServiceClient = brtServiceClient;
    }

    public String setTariffForSubscriber(Long subscriberId, Long tariffId) {
        return brtServiceClient.setTariffForSubscriber(subscriberId,tariffId);
    }

    public FullSubscriberAndTariffInfoDTO getSubscriberInfo(Long subscriberId) {
        return brtServiceClient.getSubscriberAndTariffInfo(subscriberId);
    }

    public String createSubscriber(SubscriberDTO subscriberDTO) {
        return brtServiceClient.createSubscriber(subscriberDTO);
    }

    public String topUpBalance(Long subscriberId, TopUpDTO topUpDTO) {
        return null;
    }
}
