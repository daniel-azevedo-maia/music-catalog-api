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
                        .description("""
                                Backend REST API for managing, curating and analyzing music catalogs.
                                
                                The API supports structured registration of artists, albums, tracks and genres,
                                providing a foundation for catalog organization, metadata quality control,
                                search features and future dashboard insights.
                                """)
                        .contact(new Contact()
                                .name("Daniel Azevedo Maia")
                                .email("daniel.azevedo.maia@hotmail.com"))
                        .license(new License()
                                .name("MIT License")));
    }
}