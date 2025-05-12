package org.example.crmservice.dtos.fullSubscriberAndTariffInfo;


/**
 * DTO (Data Transfer Object), агрегирующий полную информацию об абоненте и его тарифном плане.
 *
 * @param subscriber DTO с информацией об абоненте (включая ID).
 * @param tariff DTO с информацией о тарифном плане.
 */
public record FullSubscriberAndTariffInfoDTO(
        SubscriberWithIdDTO subscriber,
        TariffDTO tariff
) {
}
