package com.barberia.appointments_service.controller;

import com.barberia.appointments_service.entity.Servicio;
import com.barberia.appointments_service.service.ServicioService;
import com.barberia.appointments_service.service.ServicioImagenService;

import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
@CrossOrigin(origins = "*")
public class ServicioController {

    private final ServicioService servicioService;
    private final ServicioImagenService servicioImagenService;

    public ServicioController(
            ServicioService servicioService,
            ServicioImagenService servicioImagenService) {
        this.servicioService = servicioService;
        this.servicioImagenService = servicioImagenService;
    }

    // Registrar servicio
    @PostMapping
    public ResponseEntity<Servicio> registrar(@RequestBody Servicio servicio) {
        return ResponseEntity.ok(servicioService.registrar(servicio));
    }

    // Listar servicios
    @GetMapping
    public ResponseEntity<List<Servicio>> listar() {
        return ResponseEntity.ok(servicioService.listar());
    }

    // Consultar servicio
    @GetMapping("/{id}")
    public ResponseEntity<Servicio> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(servicioService.buscarPorId(id));
    }

    // Actualizar servicio
    @PutMapping("/{id}")
    public ResponseEntity<Servicio> actualizar(
            @PathVariable Integer id,
            @RequestBody Servicio servicio) {
        return ResponseEntity.ok(servicioService.actualizar(id, servicio));
    }

    // Subir imagen del servicio
    @PostMapping(
            value = "/{id}/imagen",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Servicio> subirImagen(
            @PathVariable Integer id,
            @RequestParam("imagen") MultipartFile imagen) {

        String rutaImagen = servicioImagenService.guardar(imagen);

        Servicio servicio = servicioService.actualizarImagen(
                id,
                rutaImagen
        );

        return ResponseEntity.ok(servicio);
    }

    // Eliminar servicio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        servicioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}