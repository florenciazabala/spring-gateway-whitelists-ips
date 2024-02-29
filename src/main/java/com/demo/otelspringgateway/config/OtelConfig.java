package com.demo.otelspringgateway.config;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.spring.webflux.v5_3.SpringWebfluxTelemetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

@Configuration
public class OtelConfig {
    @Bean
    WebFilter telemetryFilter(OpenTelemetry openTelemetry) {
        return SpringWebfluxTelemetry.builder(openTelemetry)
                .build()
                .createWebFilterAndRegisterReactorHook();
    }
}
