package com.ssafy.Homezakaya.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://127.0.0.1:3000", "https://127.0.0.1:3000", "http://localhost:3000", "https://localhost:3000", "http://i8a606.p.ssafy.io:3000", "https://i8a606.p.ssafy.io:3000", "http://i8a606.p.ssafy.io", "https://i8a606.p.ssafy.io")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.OPTIONS.name()
                );
    }
}
