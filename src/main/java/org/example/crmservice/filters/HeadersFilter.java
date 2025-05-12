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

/**
 * Фильтр для обработки заголовков запроса, содержащих информацию о пользователе и его роли.
 * Создает и устанавливает объект {@link Authentication} в {@link SecurityContextHolder}
 * на основе этих заголовков.
 */
@Component
public class HeadersFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(HeadersFilter.class);

    /**
     * Обрабатывает входящий HTTP-запрос для извлечения информации о пользователе из заголовков.
     * Если заголовки `X-User-Id` и `X-User-Role` присутствуют, создает объект аутентификации
     * и помещает его в контекст безопасности.
     *
     * @param request HTTP-запрос.
     * @param response HTTP-ответ.
     * @param filterChain Цепочка фильтров.
     * @throws ServletException Если возникает ошибка сервлета.
     * @throws IOException Если возникает ошибка ввода-вывода.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userId = request.getHeader("X-User-Id");
        if (Objects.nonNull(userId) && Objects.nonNull(request.getHeader("X-User-Role"))){
            GrantedAuthority role = new SimpleGrantedAuthority(request.getHeader("X-User-Role"));

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    List.of(role)
            );
            SecurityContext securityContext = new SecurityContextImpl(authentication);
            SecurityContextHolder.setContext(securityContext);
        }
        filterChain.doFilter(request, response);
    }
}
