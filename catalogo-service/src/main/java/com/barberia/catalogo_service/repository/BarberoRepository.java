package com.barberia.catalog_service.repository;

import com.barberia.catalog_service.entity.Barbero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BarberoRepository extends JpaRepository<Barbero, Integer> {

    Optional<Barbero> findByIdUsuario(Integer idUsuario);

    List<Barbero> findByEstadoIgnoreCase(String estado);
}