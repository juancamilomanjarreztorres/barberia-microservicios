package com.barberia.catalog_service.controller;

import com.barberia.catalog_service.dto.ClienteRequest;
import com.barberia.catalog_service.entity.Cliente;
import com.barberia.catalog_service.service.ClienteService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<?> registrarCliente(
            @Valid @RequestBody ClienteRequest request) {

        try {
            Cliente cliente = clienteService.registrarCliente(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(cliente);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> buscarPorIdUsuario(
            @PathVariable Integer idUsuario) {

        try {
            Cliente cliente = clienteService.buscarPorIdUsuario(idUsuario);
            return ResponseEntity.ok(cliente);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer idCliente) {
        try {
            Cliente cliente = clienteService.buscarPorId(idCliente);
            return ResponseEntity.ok(cliente);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

}