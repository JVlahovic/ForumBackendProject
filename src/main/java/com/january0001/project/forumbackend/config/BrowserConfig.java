package com.january0001.project.forumbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class BrowserConfig {

    //CORS is gonna be a problem if I do not solve it. This configuration file does not belong in the same spectrum as Security.
    //Hence it is being made here.

    //Now we import vercel's allowlink so I can deploy fully.

    @Value("${app.cors.allowed-origins}")
    private List<String> allowedOrigins;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOriginPatterns(allowedOrigins.toArray(new String[0]))
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                        .allowedHeaders("*");
            }
        };
    }
}
