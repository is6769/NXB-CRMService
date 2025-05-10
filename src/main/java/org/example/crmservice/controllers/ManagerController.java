package org.example.crmservice.controllers;

import jakarta.validation.Valid;
import org.example.crmservice.dtos.SubscriberDTO;
import org.example.crmservice.dtos.TopUpDTO;
import org.example.crmservice.dtos.fullSubscriberAndTariffInfo.FullSubscriberAndTariffInfoDTO;
import org.example.crmservice.dtos.fullSubscriberAndTariffInfo.SubscriberWithIdDTO;
import org.example.crmservice.services.SubscribersService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    private final SubscribersService subscribersService;

    public ManagerController(SubscribersService subscribersService) {
        this.subscribersService = subscribersService;
    }

    @PostMapping("/subscriber")
    public SubscriberWithIdDTO createSubscriber(@Valid @RequestBody SubscriberDTO subscriberDTO){
        return subscribersService.createSubscriber(subscriberDTO);
    }

    @PutMapping("/subscribers/{subscriberId}/tariff/{tariffId}")
    public String setTariffForSubscriber(@PathVariable Long subscriberId, @PathVariable Long tariffId){
        return subscribersService.setTariffForSubscriber(subscriberId,tariffId);
    }

    @PatchMapping("subscribers/{subscriberId}/balance")
    public String topUpBalance(@PathVariable Long subscriberId,@Valid @RequestBody TopUpDTO topUpDTO){
        return subscribersService.topUpBalance(subscriberId,topUpDTO);
    }

    @GetMapping("subscribers/{subscriberId}")
    public FullSubscriberAndTariffInfoDTO getSubscriberAndTariffInfo(@PathVariable Long subscriberId){
        return subscribersService.getSubscriberInfo(subscriberId);
    }
}
