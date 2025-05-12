package org.example.crmservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) для создания нового абонента.
 * Содержит основную информацию об абоненте, необходимую для его регистрации.
 *
 * @param msisdn Номер телефона абонента (MSISDN). Обязательное поле, должно содержать только цифры.
 * @param firstName Имя абонента. Обязательное поле.
 * @param secondName Отчество абонента. Необязательное поле.
 * @param surname Фамилия абонента. Обязательное поле.
 * @param tariffId Идентификатор тарифного плана. Необязательное поле; если указано, должно быть положительным.
 * @param balance Начальный баланс абонента. Необязательное поле; если указано, должно быть положительным или нулевым.
 */
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
