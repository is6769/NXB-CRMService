package org.example.crmservice.dtos;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) для операций пополнения баланса.
 * Содержит информацию о сумме и единице измерения для пополнения.
 *
 * @param amount Сумма пополнения. Должна быть предоставлена и быть не меньше 0.1.
 * @param unit Единица измерения (например, "y.e."). Должна быть предоставлена и не быть пустой.
 */
public record TopUpDTO(
        @NotNull
        @DecimalMin(value = "0.1", inclusive = true, message = "Must be provided. Must be >=0.1")
        BigDecimal amount,

        @NotBlank
        String unit
) {
}
