package org.example.crmservice.services;

import org.example.crmservice.clients.BRTServiceClient;
import org.example.crmservice.dtos.SubscriberDTO;
import org.example.crmservice.dtos.TopUpDTO;
import org.example.crmservice.dtos.fullSubscriberAndTariffInfo.FullSubscriberAndTariffInfoDTO;
import org.example.crmservice.dtos.fullSubscriberAndTariffInfo.SubscriberWithIdDTO;
import org.springframework.stereotype.Service;

/**
 * Сервис для управления операциями, связанными с абонентами.
 * Делегирует вызовы соответствующему клиенту BRT-сервиса.
 */
@Service
public class SubscribersService {

    private final BRTServiceClient brtServiceClient;

    public SubscribersService(BRTServiceClient brtServiceClient) {
        this.brtServiceClient = brtServiceClient;
    }

    /**
     * Устанавливает тариф для указанного абонента.
     * @param subscriberId Идентификатор абонента.
     * @param tariffId Идентификатор тарифа.
     * @return Строка с результатом операции.
     */
    public String setTariffForSubscriber(Long subscriberId, Long tariffId) {
        return brtServiceClient.setTariffForSubscriber(subscriberId,tariffId);
    }

    /**
     * Получает полную информацию об абоненте и его тарифе.
     * @param subscriberId Идентификатор абонента.
     * @return DTO с полной информацией об абоненте и тарифе.
     */
    public FullSubscriberAndTariffInfoDTO getSubscriberInfo(Long subscriberId) {
        return brtServiceClient.getSubscriberAndTariffInfo(subscriberId);
    }

    /**
     * Создает нового абонента.
     * @param subscriberDTO DTO с данными нового абонента.
     * @return DTO с данными созданного абонента, включая его идентификатор.
     */
    public SubscriberWithIdDTO createSubscriber(SubscriberDTO subscriberDTO) {
        return brtServiceClient.createSubscriber(subscriberDTO);
    }

    /**
     * Пополняет баланс указанного абонента.
     * @param subscriberId Идентификатор абонента.
     * @param topUpDTO DTO с информацией о сумме пополнения.
     * @return Строка с результатом операции.
     */
    public String topUpBalance(Long subscriberId, TopUpDTO topUpDTO) {
        return brtServiceClient.topUpBalance(subscriberId,topUpDTO);
    }
}
