package com.barberia.catalogo_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.barberia")
@EnableJpaRepositories(basePackages = "com.barberia.catalog_service.repository")
@EntityScan(basePackages = "com.barberia.catalog_service.entity")
public class CatalogoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatalogoServiceApplication.class, args);
	}

}