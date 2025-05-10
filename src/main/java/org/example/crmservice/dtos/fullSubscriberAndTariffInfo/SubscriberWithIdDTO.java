package org.example.crmservice.dtos.fullSubscriberAndTariffInfo;

import java.math.BigDecimal;

public record SubscriberWithIdDTO(
        Long id,
        String msisdn,
        String firstName,
        String secondName,
        String surname,
        Long tariffId,
        BigDecimal balance
) {
}
