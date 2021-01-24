package com.vartdalen.imagestoreweb.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Component
public class VueRoutePathFilter implements WebFilter {

    private final String[] BOOT_PATHS = {
            //api
            "/actuator",                //  /health, /info
            "/api",                     //  /images

            //api-docs
            "/v3",                      //  /api-docs
            "/swagger-ui.html",
            "/webjars",

            //static
            "/css",
            "/fonts",
            "/img",
            "/js",
            "/favicon.ico"
    };
    private final String SPA_PATH = "/index.html";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (isApiPath(request.getURI().getPath())) {
            return chain.filter(exchange);
        }
        return chain.filter(
                exchange
                .mutate()
                .request(
                        request
                        .mutate()
                        .path(SPA_PATH)
                        .build())
                .build());
    }

    private boolean isApiPath(String requestPath) {
        return Arrays.stream(BOOT_PATHS).anyMatch(requestPath::startsWith);
    }
}
