package org.example.crmservice.dtos.fullSubscriberAndTariffInfo;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object), представляющий правило тарификации в рамках пакета услуг.
 *
 * @param id Идентификатор правила.
 * @param ruleType Тип правила (например, "стоимость", "лимит").
 * @param value Значение правила.
 * @param unit Единица измерения для значения (например, "руб.", "МБ").
 * @param condition DTO, описывающее условие применения правила.
 */
public record PackageRuleDTO(
        Long id,
        String ruleType,
        BigDecimal value,
        String unit,
        ConditionNodeDTO condition
) {
}
