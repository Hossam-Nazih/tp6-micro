package com.example.studentmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Facultatif : springdoc documente déjà tous les contrôleurs sans configuration.
 * Ce bean ne fait que remplacer le titre générique "OpenAPI definition" en tête de la page Swagger.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI documentationApi() {
        return new OpenAPI().info(new Info()
                .title("API Gestion des étudiants")
                .version("1.0")
                .description("Étudiants, filières et modules d'enseignement. "
                        + "Les repositories sont aussi exposés automatiquement par Spring Data REST sous /api/data."));
    }
}
