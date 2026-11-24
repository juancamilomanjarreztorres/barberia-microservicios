package com.barberia.appointments_service.controller;

import com.barberia.appointments_service.entity.Horario;
import com.barberia.appointments_service.service.HorarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horarios")
@CrossOrigin(origins = "*")
public class HorarioController {

    private final HorarioService horarioService;

    public HorarioController(HorarioService horarioService) {
        this.horarioService = horarioService;
    }

    @PostMapping
    public ResponseEntity<Horario> registrar(@RequestBody Horario horario) {
        Horario nuevoHorario = horarioService.registrar(horario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoHorario);
    }

    @GetMapping
    public ResponseEntity<List<Horario>> listar() {
        return ResponseEntity.ok(horarioService.listar());
    }

    @GetMapping("/barbero/{idBarbero}")
    public ResponseEntity<List<Horario>> listarPorBarbero(
            @PathVariable Integer idBarbero) {
        return ResponseEntity.ok(horarioService.listarPorBarbero(idBarbero));
    }

    @GetMapping("/barbero/{idBarbero}/dia/{dia}")
    public ResponseEntity<List<Horario>> listarPorBarberoYDia(
            @PathVariable Integer idBarbero,
            @PathVariable String dia) {
        return ResponseEntity.ok(
                horarioService.listarPorBarberoYDia(idBarbero, dia)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        horarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}