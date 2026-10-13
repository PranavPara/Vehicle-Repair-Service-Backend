package com.example.vehicleservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// The website and this server run on different addresses.
// Browsers block this by default, so we tell the server which website is allowed.
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    // read from application.properties (app.cors.allowed-origins)
    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigins.split(","))
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
