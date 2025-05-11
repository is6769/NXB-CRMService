package org.example.crmservice.filters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;

class HeadersFilterTest {

    private HeadersFilter headersFilter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;

    @BeforeEach
    void setUp() {
        headersFilter = new HeadersFilter();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_withValidHeaders_setsAuthentication() throws Exception {
        request.addHeader("X-User-Id", "42");
        request.addHeader("X-User-Role", "ROLE_MANAGER");

        headersFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals("42", authentication.getPrincipal());
        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER")));
    }

    @Test
    void doFilterInternal_withMissingHeaders_noAuthentication() throws Exception {
        headersFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

    @Test
    void doFilterInternal_withPartialHeaders_noAuthentication() throws Exception {
        request.addHeader("X-User-Id", "42");

        headersFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }
    
    @Test
    void doFilterInternal_continuesFilterChain() throws Exception {
        headersFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(filterChain.getRequest());
        assertNotNull(filterChain.getResponse());
    }
}
