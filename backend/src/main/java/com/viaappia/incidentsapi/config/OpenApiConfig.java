package com.viaappia.incidentsapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Via Appia - Incidents API",
                version = "1.0",
                description = "API de Gestão de Ocorrências. Para autenticar: faça POST em /auth/login " +
                        "com email e senha, copie o token retornado, clique em 'Authorize' aqui no Swagger " +
                        "e cole no formato: Bearer SEU_TOKEN"
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@SecurityRequirement(name = "bearerAuth")
public class OpenApiConfig {
}