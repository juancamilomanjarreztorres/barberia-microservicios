
package com.barberia.appointments_service.controller;

import com.barberia.appointments_service.entity.Cita;
import com.barberia.appointments_service.service.CitaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    // Registrar cita
    @PostMapping
    public ResponseEntity<Cita> registrar(@RequestBody Cita cita) {
        return ResponseEntity.ok(citaService.registrar(cita));
    }

    // Listar citas
    @GetMapping
    public ResponseEntity<List<Cita>> listar() {
        return ResponseEntity.ok(citaService.listar());
    }

    // Buscar cita por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cita> buscar(@PathVariable Integer id) {
        return citaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar cita
    @PutMapping("/{id}")
    public ResponseEntity<Cita> actualizar(
            @PathVariable Integer id,
            @RequestBody Cita cita) {
        return ResponseEntity.ok(citaService.actualizar(id, cita));
    }

    // Cancelar cita
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Integer id) {
        citaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}