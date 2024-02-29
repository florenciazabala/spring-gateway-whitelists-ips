package com.demo.otelspringgateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.support.ipresolver.XForwardedRemoteAddressResolver;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

@Primary
@Component
public class ProxiedClientAddressResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxiedClientAddressResolver.class);

    //@Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        LOGGER.info("IP Proxied Client addressResolver: " + exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
        XForwardedRemoteAddressResolver resolver = XForwardedRemoteAddressResolver.maxTrustedIndex(1);
        InetSocketAddress inetSocketAddress = resolver.resolve(exchange);
        return Mono.just(inetSocketAddress.getAddress().getHostAddress());
    }
}
