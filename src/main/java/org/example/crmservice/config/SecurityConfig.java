package org.example.crmservice.config;

import org.example.crmservice.filters.HeadersFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Конфигурация безопасности для CRM-сервиса.
 * Определяет правила доступа к эндпоинтам и настраивает цепочку фильтров безопасности.
 */
@Configuration
public class SecurityConfig {

    /**
     * Настраивает цепочку фильтров безопасности Spring Security.
     * - Отключает CSRF и CORS.
     * - Добавляет {@link HeadersFilter} перед {@link BasicAuthenticationFilter} для обработки заголовков аутентификации.
     * - Отключает стандартные механизмы входа (formLogin, httpBasic).
     * - Определяет правила авторизации для эндпоинтов:
     *   - `/manager/**` требует роль "MANAGER".
     *   - `/subscriber/**` требует роль "SUBSCRIBER".
     *   - Все остальные запросы разрешены.
     * - Устанавливает политику управления сессиями как STATELESS.
     *
     * @param http Конфигуратор {@link HttpSecurity}.
     * @param headersFilter Фильтр для обработки заголовков аутентификации.
     * @return Сконфигурированный {@link SecurityFilterChain}.
     * @throws Exception Если возникает ошибка при конфигурации.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HeadersFilter headersFilter) throws Exception {
        http.cors(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.addFilterBefore(headersFilter, BasicAuthenticationFilter.class);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests( c ->
                c
                        .requestMatchers("/manager/**").hasRole("MANAGER")
                        .requestMatchers("/subscriber/**").hasRole("SUBSCRIBER")
                        .anyRequest().permitAll()
        );
        http.sessionManagement( c->{
            c.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });
        return http.build();
    }
}
