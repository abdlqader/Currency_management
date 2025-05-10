package com.example.sanad.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Value("${spring.server.url:http://localhost:8080/}")
    String serverUrl;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("API Documentation")
                        .version("1.0.0")
                        .description("sanad Detailed APIs Documentation"))
                .addServersItem(new Server().url(serverUrl));
    }
}
