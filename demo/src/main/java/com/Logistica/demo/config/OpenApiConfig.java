package com.Logistica.demo.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API Logística - Donaton")
                .version("1.0.0")
                .description("Sistema de gestión de transporte y distribución de alimentos donados")
                .contact(new Contact()
                    .name("Soporte Donaton")
                    .email("soporte@donaton.com")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("logistica")
            .pathsToMatch("/api/logistica/**")
            .build();
    }
}
