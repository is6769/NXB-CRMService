package org.example.crmservice.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
public class HeadersFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(HeadersFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("HEADERS FILTER TRIGGERED");
        String userId = request.getHeader("X-User-Id");
        if (Objects.nonNull(userId)){
            GrantedAuthority role = new SimpleGrantedAuthority(request.getHeader("X-User-Role"));

            //if (userId.equals("MANAGER"))
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    List.of(role)
            );
            log.info(authentication.toString());
            SecurityContext securityContext = new SecurityContextImpl(authentication);
            SecurityContextHolder.setContext(securityContext);
            //}
        }
        filterChain.doFilter(request, response);
    }
}
