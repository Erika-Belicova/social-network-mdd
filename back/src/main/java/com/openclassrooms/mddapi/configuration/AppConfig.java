package com.openclassrooms.mddapi.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Application-wide configuration class for customizing Spring MVC settings.
 * Configures Cross-Origin Resource Sharing (CORS) to allow frontend applications
 * (e.g., on localhost:4200) to communicate with the backend API.
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Authorization", "Content-Type")
                .allowCredentials(true);
    }

}
