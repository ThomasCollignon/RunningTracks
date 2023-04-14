package org.coli.routegenerator.restapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * When simply using @CrossOrigin with no url in the controller, Sonarqube complains that it's too permissive. The
 * solution was to add an url as argument, but it can't be done by injecting a property. This class serves this sole
 * purpose, and appeases Sonarqube.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${frontend-host:}") // Strangely a default blank value is mandatory for the tests
    private String frontendHost;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(frontendHost);
    }
}

