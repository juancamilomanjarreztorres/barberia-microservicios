package com.barberia.appointments_service.controller;

import com.barberia.appointments_service.entity.DetalleCita;
import com.barberia.appointments_service.service.DetalleCitaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalles-cita")
@CrossOrigin(origins = "*")
public class DetalleCitaController {

    private final DetalleCitaService detalleCitaService;

    public DetalleCitaController(DetalleCitaService detalleCitaService) {
        this.detalleCitaService = detalleCitaService;
    }

    @PostMapping
    public ResponseEntity<DetalleCita> registrar(
            @RequestBody DetalleCita detalle) {

        DetalleCita nuevoDetalle = detalleCitaService.registrar(detalle);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevoDetalle);
    }

    @GetMapping("/cita/{idCita}")
    public ResponseEntity<List<DetalleCita>> listarPorCita(
            @PathVariable Integer idCita) {

        return ResponseEntity.ok(
                detalleCitaService.listarPorCita(idCita)
        );
    }
}