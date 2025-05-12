package org.example.crmservice.config;

import org.example.crmservice.controllers.ManagerController;
import org.example.crmservice.controllers.SubscriberController;
import org.example.crmservice.services.SubscribersService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тестовый класс для {@link SecurityConfig}.
 * Проверяет доступность эндпоинтов для различных ролей пользователей.
 */
@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private SubscribersService subscribersService;

    /**
     * Тестирует доступность эндпоинтов менеджера для пользователя с ролью "MANAGER".
     * Ожидается, что все эндпоинты менеджера будут доступны (статус 200 OK).
     * @throws Exception если возникает ошибка при выполнении MockMvc запроса.
     */
    @Test
    void managerEndpoints_accessibleByManager() throws Exception {
        ManagerController managerController = new ManagerController(subscribersService);
        when(subscribersService.getSubscriberInfo(anyLong())).thenReturn(null);
        when(subscribersService.setTariffForSubscriber(anyLong(), anyLong())).thenReturn("OK");
        when(subscribersService.topUpBalance(anyLong(), any())).thenReturn("OK");

        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("1", null, 
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_MANAGER"))));

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(managerController)
                .build();

        mockMvc.perform(get("/manager/subscribers/1"))
                .andExpect(status().isOk());
        
        mockMvc.perform(put("/manager/subscribers/1/tariff/2"))
                .andExpect(status().isOk());
                
        mockMvc.perform(patch("/manager/subscribers/1/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":100.00,\"unit\":\"y.e.\"}"))
                .andExpect(status().isOk());
    }
    
    /**
     * Тестирует доступность эндпоинтов абонента для пользователя с ролью "SUBSCRIBER".
     * Ожидается, что эндпоинт пополнения баланса будет доступен (статус 200 OK).
     * @throws Exception если возникает ошибка при выполнении MockMvc запроса.
     */
    @Test
    void subscriberEndpoints_accessibleBySubscriber() throws Exception {
        SubscriberController subscriberController = new SubscriberController(subscribersService);
        when(subscribersService.topUpBalance(anyLong(), any())).thenReturn("OK");

        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("1", null, 
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_SUBSCRIBER"))));

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(subscriberController)
                .setCustomArgumentResolvers(new SecurityConfigTest.AuthenticationPrincipalResolver())
                .build();

        mockMvc.perform(patch("/subscriber/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":100.00,\"unit\":\"y.e.\"}"))
                .andExpect(status().isOk());
    }

    /**
     * Вспомогательный класс для разрешения аргументов, аннотированных {@link AuthenticationPrincipal}.
     * Используется для имитации поведения Spring Security в тестах.
     */
    private static class AuthenticationPrincipalResolver implements HandlerMethodArgumentResolver {
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.getParameterAnnotation(AuthenticationPrincipal.class) != null;
        }

        @Override
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            }
            return null;
        }
    }
}
