package org.example.crmservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record TopUpDTO(
        @NotNull
        @PositiveOrZero(message = "If provided, must be positive or zero")
        BigDecimal amount,

        @NotBlank
        String unit
) {
}
