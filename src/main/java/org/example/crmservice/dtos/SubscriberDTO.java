package org.example.crmservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;

public record SubscriberDTO(
        @NotBlank
        @Pattern(regexp = "^\\d*\\.?\\d+$", message = "Must be a positive number")
        String msisdn,

        @NotBlank
        String firstName,

        @Nullable
        String secondName,

        @NotBlank
        String surname,

        @Nullable
        @Positive(message = "If provided, must be positive")
        Long tariffId,

        @Nullable
        @PositiveOrZero(message = "If provided, must be positive or zero")
        BigDecimal balance
) {}
