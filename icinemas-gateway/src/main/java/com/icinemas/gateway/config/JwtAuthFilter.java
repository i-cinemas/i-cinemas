package com.icinemas.gateway.config;

import com.icinemas.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.icinemas.constants.AuthConstants.AUTH_BEARER_PREFIX;
import static com.icinemas.constants.AuthConstants.JWT_USER_ROLE;

@Slf4j
@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {


    public JwtAuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(final Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith(AUTH_BEARER_PREFIX)) {
                return this.onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);

            try {
                if (!JwtUtil.validateToken(token)) {
                    return this.onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
                }

                Claims claims = JwtUtil.getAllClaimsFromToken(token);
                String role = claims.get(JWT_USER_ROLE, String.class);

                if (config.getAllowedRoles() != null && !config.getAllowedRoles().isEmpty()) {
                    boolean allowed = config.getAllowedRoles().stream()
                            .anyMatch(r -> r.equalsIgnoreCase(role));
                    if (!allowed) {
                        return this.onError(exchange, "Forbidden - insufficient role", HttpStatus.FORBIDDEN);
                    }
                }

                // optionally attach userId/role into request headers for downstream services
                exchange = exchange.mutate()
                        .request(r -> r.headers(h -> {
                            h.add("X-User-Id", claims.getSubject());
                            h.add("X-User-Role", role);
                        }))
                        .build();

            } catch (Exception ex) {
                log.error("JWT validation failed", ex);
                return this.onError(exchange, "Invalid JWT", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
        log.warn("Request blocked: {}", err);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }

    @Data
    public static class Config {
        private List<String> allowedRoles;
    }
}
