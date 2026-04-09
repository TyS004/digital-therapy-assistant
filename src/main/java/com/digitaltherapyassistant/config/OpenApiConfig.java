package com.digitaltherapyassistant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI digitalTherapyOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Commission Calculator API")
                .description("Spring Boot Fundamentals — Commission Calculator REST API\n\n"
                    + "This API provides endpoints for managing deals, commission plans, "
                    + "commission calculations, users, and disputes.\n\n"
                    + "**Authentication:** Use the `/api/auth/login` endpoint to obtain a JWT token, "
                    + "then click the 'Authorize' button above and enter: `Bearer <your-token>`")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Tyler Stier")
                    .email("tstier@chapman.edu"))
                .license(new License()
                    .name("Educational Use")))
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            .components(new Components()
                .addSecuritySchemes("Bearer Authentication",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Enter the JWT token obtained from POST /api/auth/login")));
    }
}