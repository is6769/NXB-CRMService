package org.example.crmservice.dtos.fullSubscriberAndTariffInfo;

import java.util.List;

public record ConditionNodeDTO(
        String type,
        String field,
        String operator,
        String value,
        List<ConditionNodeDTO> conditions
) {
}