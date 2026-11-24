package com.barberia.catalog_service.service;

import com.barberia.catalog_service.dto.AdministradorRequest;
import com.barberia.catalog_service.entity.Administrador;
import com.barberia.catalog_service.repository.AdministradorRepository;

import org.springframework.stereotype.Service;

@Service
public class AdministradorService {

    private final AdministradorRepository administradorRepository;

    public AdministradorService(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    public Administrador registrarAdministrador(AdministradorRequest request) {

        if (administradorRepository.findByIdUsuario(request.idUsuario()).isPresent()) {
            throw new RuntimeException("Este usuario ya tiene un perfil de administrador");
        }

        Administrador administrador = new Administrador();

        administrador.setNombre(request.nombre());
        administrador.setNivelAcceso(
                request.nivelAcceso() == null || request.nivelAcceso().isBlank()
                        ? "Básico"
                        : request.nivelAcceso()
        );
        administrador.setArea(request.area());
        administrador.setIdUsuario(request.idUsuario());

        return administradorRepository.save(administrador);
    }
}
