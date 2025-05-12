package org.example.crmservice.dtos.fullSubscriberAndTariffInfo;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object), представляющий информацию об абоненте, включая его идентификатор.
 * Используется для возврата данных о созданном или существующем абоненте.
 *
 * @param id Уникальный идентификатор абонента.
 * @param msisdn Номер телефона абонента (MSISDN).
 * @param firstName Имя абонента.
 * @param secondName Отчество абонента.
 * @param surname Фамилия абонента.
 * @param tariffId Идентификатор тарифного плана абонента.
 * @param balance Текущий баланс абонента.
 */
public record SubscriberWithIdDTO(
        Long id,
        String msisdn,
        String firstName,
        String secondName,
        String surname,
        Long tariffId,
        BigDecimal balance
) {
}
