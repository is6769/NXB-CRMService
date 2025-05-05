package org.example.crmservice.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
public class HeadersFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String userId = httpRequest.getHeader("X-User-Id");
        if (Objects.nonNull(userId)){
            GrantedAuthority role = new SimpleGrantedAuthority(httpRequest.getHeader("role"));

            //if (userId.equals("MANAGER"))
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    List.of(role)
            );
            SecurityContext securityContext = new SecurityContextImpl(authentication);
            SecurityContextHolder.setContext(securityContext);
            //}
        }
        chain.doFilter(request, response);
    }
}
