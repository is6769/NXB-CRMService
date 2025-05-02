package org.example.crmservice.controllers;

import org.example.crmservice.dtos.SubscriberDTO;
import org.example.crmservice.dtos.TopUpDTO;
import org.example.crmservice.services.SubscribersService;
import org.springframework.web.bind.annotation.*;

@RestController
public class SubscribersController {

    private final SubscribersService subscribersService;

    public SubscribersController(SubscribersService subscribersService) {
        this.subscribersService = subscribersService;
    }

    @PostMapping("/subscriber")
    public String createSubscriber(@RequestBody SubscriberDTO subscriberDTO){
        return subscribersService.createSubscriber(subscriberDTO);
    }

    @PutMapping("/subscribers/{subscriberId}/tariff/{tariffId}")
    public String setTariffForSubscriber(@PathVariable Long subscriberId, @PathVariable Long tariffId){
        return subscribersService.setTariffForSubscriber(subscriberId,tariffId);
    }

    @PatchMapping("subscribers/{subscriberId}/balance")
    public String topUpBalance(@PathVariable Long subscriberId, @RequestParam TopUpDTO topUpDTO){
        return subscribersService.topUpBalance(subscriberId,topUpDTO);
    }

    @GetMapping("subscribers/{subscriberId}")
    public String getSubscriberInfo(@PathVariable Long subscriberId){
        return subscribersService.getSubscriberInfo(subscriberId);
    }
}
