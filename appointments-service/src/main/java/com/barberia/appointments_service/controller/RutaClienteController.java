package com.barberia.appointments_service.controller;

import com.barberia.appointments_service.service.ClienteCatalogoService;
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
public class RutaClienteController {

    private final ClienteCatalogoService clienteService;
    private final OsrmService osrmService;

    public RutaClienteController(
            ClienteCatalogoService clienteService,
            OsrmService osrmService) {
        this.clienteService = clienteService;
        this.osrmService = osrmService;
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<?> calcularRutaCliente(
            @PathVariable Integer idCliente) {

        try {
            JsonNode cliente = clienteService.obtenerCliente(idCliente);

            BigDecimal latitud = new BigDecimal(
                    cliente.get("latitud").asText());

            BigDecimal longitud = new BigDecimal(
                    cliente.get("longitud").asText());

            JsonNode ruta = osrmService.calcularRuta(latitud, longitud);

            Map<String, Object> resultado = new HashMap<>();
            resultado.put("idCliente", idCliente);
            resultado.put("nombre", cliente.path("nombre").asText());
            resultado.put("distanciaMetros",
                    ruta.path("distance").asDouble());
            resultado.put("duracionSegundos",
                    ruta.path("duration").asDouble());
            resultado.put("duracionMinutos",
                    Math.ceil(ruta.path("duration").asDouble() / 60));

            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error calculando ruta del cliente: " + e.getMessage());
        }
    }
}