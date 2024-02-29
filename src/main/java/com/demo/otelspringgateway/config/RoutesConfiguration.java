package com.demo.otelspringgateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;

import java.util.ArrayList;
import java.util.List;

@Configuration
class RoutesConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxiedClientAddressResolver.class);

    @Bean
    @Order(-1)
    public GlobalFilter whitelistFilter() {
        return (exchange, chain) -> {
            // verify request remote address
            List<String> whitelist = new ArrayList<>();
            whitelist.add("0:0:0:0:0:0:0:1");
            String id = exchange.getRequest().getRemoteAddress().getHostName();
            new SimpleClientAddressResolver().resolve(exchange);
            new ProxiedClientAddressResolver().resolve(exchange);
            if (!whitelist.contains(id)) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            return chain.filter(exchange);
        };
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        new SimpleClientAddressResolver();
        new ProxiedClientAddressResolver();
        return builder.routes()
                .route(p -> p
                        .path("/get")
                        .uri("http://httpbin.org:80"))
                .build();
    }
}
