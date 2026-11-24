package com.barberia.appointments_service.repository;

import com.barberia.appointments_service.entity.DetalleCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleCitaRepository extends JpaRepository<DetalleCita, Integer> {

    List<DetalleCita> findByIdCita(Integer idCita);

}