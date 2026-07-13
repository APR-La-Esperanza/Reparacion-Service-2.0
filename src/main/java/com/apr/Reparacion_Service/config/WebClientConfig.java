package com.apr.Reparacion_Service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${service.incidencia.url:http://incidencia-service}")
    private String incidenciaServiceUrl;

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient webClient(WebClient.Builder loadBalancedWebClientBuilder) {
        if (incidenciaServiceUrl.contains(".") || incidenciaServiceUrl.contains("localhost")) {
            return WebClient.builder().baseUrl(incidenciaServiceUrl).build();
        }
        return loadBalancedWebClientBuilder.baseUrl(incidenciaServiceUrl).build();
    }
}
