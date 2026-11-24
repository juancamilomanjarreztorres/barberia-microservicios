package com.barberia.appointments_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String directorioImagenes;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String ruta = Paths.get(directorioImagenes)
                .toAbsolutePath()
                .normalize()
                .toUri()
                .toString();

        registry.addResourceHandler("/uploads/servicios/**")
                .addResourceLocations(ruta);
    }
}