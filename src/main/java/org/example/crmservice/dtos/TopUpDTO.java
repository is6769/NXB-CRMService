package org.example.crmservice.dtos;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TopUpDTO(
        @NotNull
        @PositiveOrZero(message = "If provided, must be positive or zero")
        @DecimalMin(value = "0.1", inclusive = true)
        BigDecimal amount,

        @NotBlank
        String unit
) {
}
