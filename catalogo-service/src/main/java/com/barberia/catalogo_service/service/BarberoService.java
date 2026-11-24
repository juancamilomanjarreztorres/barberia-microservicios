package com.barberia.catalog_service.service;

import com.barberia.catalog_service.dto.BarberoRequest;
import com.barberia.catalog_service.entity.Barbero;
import com.barberia.catalog_service.repository.BarberoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BarberoService {

    private final BarberoRepository barberoRepository;

    public BarberoService(BarberoRepository barberoRepository) {
        this.barberoRepository = barberoRepository;
    }

    @Transactional
    public Barbero registrarBarbero(BarberoRequest request) {

        if (barberoRepository.findByIdUsuario(request.idUsuario()).isPresent()) {
            throw new RuntimeException(
                    "Este usuario ya tiene un perfil de barbero registrado."
            );
        }

        Barbero barbero = new Barbero();
        barbero.setNombre(request.nombre());
        barbero.setEspecialidad(request.especialidad());
        barbero.setTelefono(request.telefono());
        barbero.setIdUsuario(request.idUsuario());
        barbero.setEstado("Activo");

        return barberoRepository.save(barbero);
    }

    public List<Barbero> listarBarberos() {
        return barberoRepository.findAll();
    }

    public List<Barbero> listarBarberosActivos() {
        return barberoRepository.findByEstadoIgnoreCase("Activo");
    }
}