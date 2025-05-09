package org.example.crmservice.clients;

import org.example.crmservice.dtos.SubscriberDTO;
import org.example.crmservice.dtos.TopUpDTO;
import org.example.crmservice.dtos.fullSubscriberAndTariffInfo.FullSubscriberAndTariffInfoDTO;
import org.example.crmservice.dtos.fullSubscriberAndTariffInfo.SubscriberWithIdDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;

@Component
public class BRTServiceClient {

    private static final Logger log = LoggerFactory.getLogger(BRTServiceClient.class);
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
                .uri(BASE_URL, uriBuilder -> uriBuilder
                        .path("/subscribers/{subscriberId}/tariff/{tariffId}")
                        .build(subscriberId, tariffId))
                .retrieve()
//                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
//                    log.info(response.getHeaders().toString());
//                    throw new HttpClientErrorException(
//                            response.getStatusCode(),
//                            response.getStatusText(),
//                            response.getHeaders(),
//                            response.getBody().readAllBytes(),
//                            null
//                    );
//                }))
//                .onStatus(HttpStatusCode::is5xxServerError, ((request, response) -> {
//                    throw new HttpServerErrorException(
//                            response.getStatusCode(),
//                            response.getStatusText(),
//                            response.getHeaders(),
//                            response.getBody().readAllBytes(),
//                            null
//                    );
//                }))
                .body(String.class);
    }

    public FullSubscriberAndTariffInfoDTO getSubscriberAndTariffInfo(Long subscriberId) {
        return restClientBuilder
                .build()
                .get()
                .uri(BASE_URL, uriBuilder -> uriBuilder
                        .path("/subscribers/{subscriberId}")
                        .build(subscriberId))
                .retrieve()
                .body(FullSubscriberAndTariffInfoDTO.class);
    }

    public SubscriberWithIdDTO createSubscriber(SubscriberDTO subscriberDTO) {
        return restClientBuilder
                .build()
                .post()
                .uri(BASE_URL, uriBuilder -> uriBuilder
                        .path("/subscriber")
                        .build())
                .body(subscriberDTO)
                .retrieve()
                .body(SubscriberWithIdDTO.class);
    }

    public String topUpBalance(Long subscriberId, TopUpDTO topUpDTO) {
        return restClientBuilder
                .build()
                .patch()
                .uri(BASE_URL, uriBuilder -> uriBuilder
                        .path("/subscribers/{subscriberId}/balance")
                        .build(subscriberId))
                .body(topUpDTO)
                .retrieve()
                .body(String.class);
    }
}
