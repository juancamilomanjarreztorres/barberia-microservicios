package com.barberia.catalog_service.service;

import com.barberia.catalog_service.dto.ClienteRequest;
import com.barberia.catalog_service.entity.Cliente;
import com.barberia.catalog_service.repository.ClienteRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Cliente registrarCliente(ClienteRequest request) {

        if (clienteRepository.findByIdUsuario(request.idUsuario()).isPresent()) {
            throw new RuntimeException(
                    "Este usuario ya tiene un perfil de cliente registrado."
            );
        }

        if (clienteRepository.findByCorreo(request.correo()).isPresent()) {
            throw new RuntimeException(
                    "Ya existe un cliente registrado con ese correo."
            );
        }

        Cliente cliente = new Cliente();

        cliente.setNombre(request.nombre());
        cliente.setTelefono(request.telefono());
        cliente.setCorreo(request.correo());
        cliente.setUbicacionActual(request.ubicacionActual());
        cliente.setLatitud(request.latitud());
        cliente.setLongitud(request.longitud());
        cliente.setIdUsuario(request.idUsuario());
        cliente.setEstado("Activo");

        return clienteRepository.save(cliente);
    }

    public Cliente buscarPorIdUsuario(Integer idUsuario) {
        return clienteRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException(
                        "No se encontró un perfil de cliente para este usuario."
                ));
    }

    public Cliente buscarPorId(Integer idCliente) {
        return clienteRepository.findById(idCliente)
                .orElseThrow(() ->
                        new RuntimeException("No se encontró el cliente."));
    }

}