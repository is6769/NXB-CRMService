package org.example.crmservice.dtos;

import org.springframework.lang.Nullable;

import java.math.BigDecimal;

public record SubscriberDTO(
    String msisdn,
    String firstName,
    @Nullable String secondName,
    String surname,
    @Nullable Long tariffId,
    @Nullable BigDecimal balance
) {}
