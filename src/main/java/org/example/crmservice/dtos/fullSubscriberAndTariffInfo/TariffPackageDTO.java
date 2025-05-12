package org.example.crmservice.dtos.fullSubscriberAndTariffInfo;

/**
 * DTO (Data Transfer Object), представляющий пакет услуг в рамках тарифа.
 *
 * @param id Идентификатор пакета тарифа.
 * @param priority Приоритет пакета услуг.
 * @param servicePackage DTO с информацией о самом пакете услуг.
 */
public record TariffPackageDTO(
        Long id,
        Integer priority,
        ServicePackageDTO servicePackage
) {
}
