package com.barberia.catalog_service.controller;

import com.barberia.catalog_service.dto.BarberoRequest;
import com.barberia.catalog_service.entity.Barbero;
import com.barberia.catalog_service.service.BarberoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barberos")
@CrossOrigin(origins = "*")
public class BarberoController {

    private final BarberoService barberoService;

    public BarberoController(BarberoService barberoService) {
        this.barberoService = barberoService;
    }

    @PostMapping
    public ResponseEntity<?> registrarBarbero(
            @Valid @RequestBody BarberoRequest request) {

        try {
            Barbero barbero = barberoService.registrarBarbero(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(barbero);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Barbero>> listarBarberos() {
        return ResponseEntity.ok(barberoService.listarBarberos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Barbero>> listarBarberosActivos() {
        return ResponseEntity.ok(barberoService.listarBarberosActivos());
    }
}