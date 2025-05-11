package org.example.crmservice.dtos;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TopUpDTO(
        @NotNull
        @DecimalMin(value = "0.1", inclusive = true, message = "Must be provided. Must be >=0.1")
        BigDecimal amount,

        @NotBlank
        String unit
) {
}
