package org.example.crmservice.controllers;

import org.example.crmservice.dtos.SubscriberDTO;
import org.example.crmservice.dtos.TopUpDTO;
import org.example.crmservice.dtos.fullSubscriberAndTariffInfo.FullSubscriberAndTariffInfoDTO;
import org.example.crmservice.services.SubscribersService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class SubscriberController {

    private final SubscribersService subscribersService;

    public SubscriberController(SubscribersService subscribersService) {
        this.subscribersService = subscribersService;
    }

//    @PatchMapping("subscriber/balance")
//    public String topUpBalance(@RequestParam TopUpDTO topUpDTO){
//        return subscribersService.topUpBalance(topUpDTO);
//    }

}
