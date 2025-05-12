package org.example.crmservice.controllers;

import jakarta.validation.Valid;
import org.example.crmservice.dtos.SubscriberDTO;
import org.example.crmservice.dtos.TopUpDTO;
import org.example.crmservice.dtos.fullSubscriberAndTariffInfo.FullSubscriberAndTariffInfoDTO;
import org.example.crmservice.dtos.fullSubscriberAndTariffInfo.SubscriberWithIdDTO;
import org.example.crmservice.services.SubscribersService;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер, обрабатывающий запросы от менеджеров.
 * Предоставляет эндпоинты для управления абонентами и их данными.
 */
@RestController
@RequestMapping("/manager")
public class ManagerController {

    private final SubscribersService subscribersService;

    public ManagerController(SubscribersService subscribersService) {
        this.subscribersService = subscribersService;
    }

    /**
     * Создает нового абонента.
     * @param subscriberDTO DTO с данными нового абонента. Должен быть валидным.
     * @return DTO с данными созданного абонента, включая его идентификатор.
     */
    @PostMapping("/subscriber")
    public SubscriberWithIdDTO createSubscriber(@Valid @RequestBody SubscriberDTO subscriberDTO){
        return subscribersService.createSubscriber(subscriberDTO);
    }

    /**
     * Устанавливает тариф для указанного абонента.
     * @param subscriberId Идентификатор абонента.
     * @param tariffId Идентификатор тарифа.
     * @return Строка с результатом операции.
     */
    @PutMapping("/subscribers/{subscriberId}/tariff/{tariffId}")
    public String setTariffForSubscriber(@PathVariable Long subscriberId, @PathVariable Long tariffId){
        return subscribersService.setTariffForSubscriber(subscriberId,tariffId);
    }

    /**
     * Пополняет баланс указанного абонента.
     * @param subscriberId Идентификатор абонента.
     * @param topUpDTO DTO с информацией о сумме пополнения. Должен быть валидным.
     * @return Строка с результатом операции.
     */
    @PatchMapping("subscribers/{subscriberId}/balance")
    public String topUpBalance(@PathVariable Long subscriberId,@Valid @RequestBody TopUpDTO topUpDTO){
        return subscribersService.topUpBalance(subscriberId,topUpDTO);
    }

    /**
     * Получает полную информацию об абоненте и его тарифе.
     * @param subscriberId Идентификатор абонента.
     * @return DTO с полной информацией об абоненте и тарифе.
     */
    @GetMapping("subscribers/{subscriberId}")
    public FullSubscriberAndTariffInfoDTO getSubscriberAndTariffInfo(@PathVariable Long subscriberId){
        return subscribersService.getSubscriberInfo(subscriberId);
    }
}
