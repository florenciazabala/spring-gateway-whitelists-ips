package com.demo.otelspringgateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Optional;

@Component
public class SimpleClientAddressResolver /*implements KeyResolver*/ {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleClientAddressResolver.class);

    //@Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        LOGGER.info("IP Client addressResolver: " + exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
        return Optional.ofNullable(exchange.getRequest().getRemoteAddress())
                .map(InetSocketAddress::getAddress)
                .map(InetAddress::getHostAddress)
                .map(Mono::just)
                .orElse(Mono.empty());
    }
}
