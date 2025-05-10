package org.example.crmservice.controllers;

import jakarta.validation.Valid;
import org.example.crmservice.dtos.TopUpDTO;
import org.example.crmservice.services.SubscribersService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriberController {

    private final SubscribersService subscribersService;

    public SubscriberController(SubscribersService subscribersService) {
        this.subscribersService = subscribersService;
    }

    @PatchMapping("subscriber/balance")
    public String topUpBalance(@RequestBody @Valid TopUpDTO topUpDTO, @AuthenticationPrincipal String ref_id){
        return subscribersService.topUpBalance(Long.valueOf(ref_id),topUpDTO);
    }

}
