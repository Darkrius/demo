package com.example.demo.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API DE LA CARPETA DEMO")
                        .version("1.0.0")
                        .description("PROBANDO TODAS LOS ENDPOINT POSIBLES")
                        .contact(new Contact()
                                .name("CONMIGO")
                                .email("JAHIR@CASJNCSAJ.pe"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
        .components(new Components()
                .addSecuritySchemes("bearer-key", // Nombre interno
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT") // Formato esperado
                                .description("Ingrese su token JWT aquí (sin la palabra Bearer)")))

                // 3. Aplicar la seguridad a TODOS los endpoints globalmente
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"));
    }
    }

