package org.example.crmservice.clients;

import org.example.crmservice.dtos.SubscriberDTO;
import org.example.crmservice.dtos.TopUpDTO;
import org.example.crmservice.dtos.fullSubscriberAndTariffInfo.FullSubscriberAndTariffInfoDTO;
import org.example.crmservice.dtos.fullSubscriberAndTariffInfo.SubscriberWithIdDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Клиент для взаимодействия с BRT-сервисом (Billing Real-Time).
 * Использует {@link RestClient} для выполнения HTTP-запросов к BRT-сервису.
 */
@Component
public class BRTServiceClient {

    private final RestClient.Builder restClientBuilder;

    @Value("${const.brt-service.BASE_URL}")
    private String BASE_URL;

    public BRTServiceClient(RestClient.Builder restClientBuilder) {
        this.restClientBuilder = restClientBuilder;
    }


    /**
     * Отправляет запрос в BRT-сервис для установки тарифа абоненту.
     * @param subscriberId Идентификатор абонента.
     * @param tariffId Идентификатор тарифа.
     * @return Строка с ответом от BRT-сервиса.
     */
    public String setTariffForSubscriber(Long subscriberId, Long tariffId){
        return restClientBuilder
                .build()
                .put()
                .uri(BASE_URL, uriBuilder -> uriBuilder
                        .path("/subscribers/{subscriberId}/tariff/{tariffId}")
                        .build(subscriberId, tariffId))
                .retrieve()
                .body(String.class);
    }

    /**
     * Отправляет запрос в BRT-сервис для получения полной информации об абоненте и его тарифе.
     * @param subscriberId Идентификатор абонента.
     * @return DTO с полной информацией об абоненте и тарифе.
     */
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

    /**
     * Отправляет запрос в BRT-сервис для создания нового абонента.
     * @param subscriberDTO DTO с данными нового абонента.
     * @return DTO с данными созданного абонента, включая его идентификатор.
     */
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

    /**
     * Отправляет запрос в BRT-сервис для пополнения баланса абонента.
     * @param subscriberId Идентификатор абонента.
     * @param topUpDTO DTO с информацией о сумме пополнения.
     * @return Строка с ответом от BRT-сервиса.
     */
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
