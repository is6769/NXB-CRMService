package org.example.crmservice.clients;

import org.example.crmservice.dtos.SubscriberDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class BRTServiceClient {

    private final RestClient.Builder restClientBuilder;

    @Value("${const.brt-service.BASE_URL}")
    private String BASE_URL;

    public BRTServiceClient(RestClient.Builder restClientBuilder) {
        this.restClientBuilder = restClientBuilder;
    }


    public String setTariffForSubscriber(Long subscriberId, Long tariffId){
        return restClientBuilder
                .build()
                .put()
                .uri(BASE_URL,uriBuilder -> uriBuilder
                        .path("/subscribers/{subscriberId}/tariff/{tariffId}")
                        .build(subscriberId,tariffId))
                .retrieve()
                .body(String.class);
    }

    public String getSubscriberAndTariffInfo(Long subscriberId) {
        return restClientBuilder
                .build()
                .get()
                .uri(BASE_URL, uriBuilder -> uriBuilder
                        .path("/subscribers/{subscriberId}")
                        .build(subscriberId))
                .retrieve()
                .body(String.class);
    }

    public String createSubscriber(SubscriberDTO subscriberDTO) {
        return restClientBuilder
                .build()
                .post()
                .uri(BASE_URL, uriBuilder -> uriBuilder
                        .path("/subscriber")
                        .build())
                .body(subscriberDTO)
                .retrieve()
                .body(String.class);
    }
}
