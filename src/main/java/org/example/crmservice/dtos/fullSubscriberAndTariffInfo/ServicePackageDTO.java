package org.example.crmservice.dtos.fullSubscriberAndTariffInfo;

import java.util.List;

/**
 * DTO (Data Transfer Object), представляющий информацию о пакете услуг.
 *
 * @param id Идентификатор пакета услуг.
 * @param name Название пакета услуг.
 * @param description Описание пакета услуг.
 * @param serviceType Тип услуги (например, "голосовая связь", "интернет").
 * @param packageRules Список правил, применяемых к пакету.
 */
public record ServicePackageDTO(
        Long id,
        String name,
        String description,
        String serviceType,
        List<PackageRuleDTO> packageRules
        //List<SubscriberPackageUsage> subscriberPackageUsages
) {
}
