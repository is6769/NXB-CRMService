package org.example.crmservice.dtos.fullSubscriberAndTariffInfo;


public record FullSubscriberAndTariffInfoDTO(
        SubscriberWithIdDTO subscriber,
        TariffDTO tariff
) {
}
