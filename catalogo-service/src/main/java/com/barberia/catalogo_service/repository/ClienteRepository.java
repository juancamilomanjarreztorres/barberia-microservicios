package com.barberia.catalog_service.repository;

import com.barberia.catalog_service.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Optional<Cliente> findByIdUsuario(Integer idUsuario);

    Optional<Cliente> findByCorreo(String correo);
}