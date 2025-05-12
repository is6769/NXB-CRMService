package org.example.crmservice.dtos.fullSubscriberAndTariffInfo;

import java.util.List;

/**
 * DTO (Data Transfer Object), представляющий информацию о тарифном плане.
 *
 * @param id Идентификатор тарифа.
 * @param name Название тарифа.
 * @param description Описание тарифа.
 * @param cycleSize Длительность тарифного цикла (например, "30 дней").
 * @param is_active Флаг, указывающий, активен ли тариф.
 * @param tariffPackages Список пакетов услуг, включенных в тариф.
 */
public record TariffDTO(
        Long id,
        String name,
        String description,
        String cycleSize,
        Boolean is_active,
        List<TariffPackageDTO> tariffPackages
) {
}
