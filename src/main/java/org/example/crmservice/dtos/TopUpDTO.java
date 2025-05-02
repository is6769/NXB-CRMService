package org.example.crmservice.dtos;

import java.math.BigDecimal;

public record TopUpDTO(
        BigDecimal amount,
        String unit
) {
}
