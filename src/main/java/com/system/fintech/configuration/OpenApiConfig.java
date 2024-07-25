package com.system.fintech.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.system.fintech.utils.Constants.OpenApi.BEARER;
import static com.system.fintech.utils.Constants.OpenApi.JWT;

@Configuration
public class OpenApiConfig {
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(AUTHORIZATION_HEADER,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme(BEARER)
                                        .bearerFormat(JWT)
                                        .name(AUTHORIZATION_HEADER)))
                .addSecurityItem(new SecurityRequirement().addList(AUTHORIZATION_HEADER));
    }
}
