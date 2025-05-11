package org.example.crmservice.controllers;

import org.example.crmservice.dtos.TopUpDTO;
import org.example.crmservice.services.SubscribersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SubscriberControllerTest {

    @Mock
    private SubscribersService subscribersService;

    @InjectMocks
    private SubscriberController subscriberController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                "42",
                null, 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_SUBSCRIBER"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        mockMvc = MockMvcBuilders.standaloneSetup(subscriberController)
                .setCustomArgumentResolvers(new AuthenticationPrincipalResolver())
                .build();
    }

    @Test
    void topUpBalance_withValidInput_returnsSuccessMessage() throws Exception {
        Long subscriberId = 42L;
        String response = "Balance updated successfully";
        
        when(subscribersService.topUpBalance(eq(subscriberId), any(TopUpDTO.class)))
                .thenReturn(response);

        mockMvc.perform(patch("/subscriber/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":100.00,\"unit\":\"y.e.\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
        
        verify(subscribersService).topUpBalance(eq(subscriberId), any(TopUpDTO.class));
    }
    
    @Test
    void topUpBalance_withMissingAmount_returnsBadRequest() throws Exception {
        mockMvc.perform(patch("/subscriber/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"unit\":\"y.e.\"}"))
                .andExpect(status().isBadRequest());
        
        verifyNoInteractions(subscribersService);
    }
    
    @Test
    void topUpBalance_withNegativeAmount_returnsBadRequest() throws Exception {
        mockMvc.perform(patch("/subscriber/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":-50.00,\"unit\":\"y.e.\"}"))
                .andExpect(status().isBadRequest());
        
        verifyNoInteractions(subscribersService);
    }
    
    @Test
    void topUpBalance_withZeroAmount_returnsBadRequest() throws Exception {
        mockMvc.perform(patch("/subscriber/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":0.0,\"unit\":\"y.e.\"}"))
                .andExpect(status().isBadRequest());
        
        verifyNoInteractions(subscribersService);
    }

    @Test
    void topUpBalance_withTooSmallAmount_returnsBadRequest() throws Exception {
        mockMvc.perform(patch("/subscriber/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":0.01,\"unit\":\"y.e.\"}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(subscribersService);
    }
    
    @Test
    void topUpBalance_withMissingUnit_returnsBadRequest() throws Exception {
        mockMvc.perform(patch("/subscriber/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":100.00}"))
                .andExpect(status().isBadRequest());
        
        verifyNoInteractions(subscribersService);
    }
    
    @Test
    void topUpBalance_withEmptyUnit_returnsBadRequest() throws Exception {
        mockMvc.perform(patch("/subscriber/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":100.00,\"unit\":\"\"}"))
                .andExpect(status().isBadRequest());
        
        verifyNoInteractions(subscribersService);
    }

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
