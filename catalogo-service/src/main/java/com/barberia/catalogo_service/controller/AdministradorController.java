package com.barberia.catalog_service.controller;

import com.barberia.catalog_service.dto.AdministradorRequest;
import com.barberia.catalog_service.entity.Administrador;
import com.barberia.catalog_service.service.AdministradorService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/administradores")
@CrossOrigin(origins = "*")
public class AdministradorController {

    private final AdministradorService administradorService;

    public AdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    @PostMapping
    public ResponseEntity<?> registrarAdministrador(
            @Valid @RequestBody AdministradorRequest request) {

        try {
            Administrador administrador =
                    administradorService.registrarAdministrador(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(administrador);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }
}
