package com.barberia.appointments_service.controller;

import com.barberia.appointments_service.service.OsrmService;
import tools.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/rutas")
@CrossOrigin(origins = "*")
public class RutaController {

    private final OsrmService osrmService;

    public RutaController(OsrmService osrmService) {
        this.osrmService = osrmService;
    }

    @GetMapping("/calcular")
    public ResponseEntity<?> calcularRuta(
            @RequestParam BigDecimal latitud,
            @RequestParam BigDecimal longitud) {

        try {
            JsonNode ruta = osrmService.calcularRuta(latitud, longitud);

            double distanciaMetros = ruta.path("distance").asDouble();
            double duracionSegundos = ruta.path("duration").asDouble();

            Map<String, Object> resultado = new HashMap<>();
            resultado.put("distanciaMetros", distanciaMetros);
            resultado.put("duracionSegundos", duracionSegundos);
            resultado.put("duracionMinutos",
                    Math.ceil(duracionSegundos / 60));

            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al calcular la ruta: " + e.getMessage());
        }
    }
}