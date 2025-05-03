package org.example.crmservice.dtos.fullSubscriberAndTariffInfo;

public record TariffPackageDTO(
        Long id,
        Integer priority,
        ServicePackageDTO servicePackage
) {
}
