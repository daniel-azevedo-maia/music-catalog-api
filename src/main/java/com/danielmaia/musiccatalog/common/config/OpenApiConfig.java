package com.danielmaia.musiccatalog.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI musicCatalogOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Music Catalog API")
                        .version("0.1.0")
                        .description("REST API for managing artists, albums, tracks and genres.")
                        .contact(new Contact()
                                .name("Daniel Azevedo Maia")
                                .email("daniel.azevedo.maia@hotmail.com"))
                        .license(new License()
                                .name("MIT License")));
    }
}