package org.example.crmservice.config;

import org.example.crmservice.filters.HeadersFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig {

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
