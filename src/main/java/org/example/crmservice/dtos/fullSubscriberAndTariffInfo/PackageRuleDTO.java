package org.example.crmservice.dtos.fullSubscriberAndTariffInfo;

import java.math.BigDecimal;

public record PackageRuleDTO(
        Long id,
        String ruleType,
        BigDecimal value,
        String unit,
        ConditionNodeDTO condition
) {
}
