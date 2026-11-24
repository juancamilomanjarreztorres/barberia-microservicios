package com.barberia.appointments_service.controller;

import com.barberia.appointments_service.dto.CalculoDesplazamientoResponse;
import com.barberia.appointments_service.service.CitaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/desplazamientos")
@CrossOrigin(origins = "*")
public class CalculoDesplazamientoController {

    private final CitaService citaService;

    public CalculoDesplazamientoController(CitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<?> calcular(
            @PathVariable Integer idCliente,
            @RequestParam String fechaHora) {

        try {
            if (idCliente <= 0) {
                return ResponseEntity.badRequest()
                        .body("El ID del cliente no es válido.");
            }

            LocalDateTime fecha = LocalDateTime.parse(fechaHora);

            CalculoDesplazamientoResponse resultado =
                    citaService.calcularDesplazamiento(idCliente, fecha);

            return ResponseEntity.ok(resultado);

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest()
                    .body("El formato de fecha y hora no es válido.");

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("No fue posible calcular el desplazamiento: "
                            + e.getMessage());
        }
    }
}