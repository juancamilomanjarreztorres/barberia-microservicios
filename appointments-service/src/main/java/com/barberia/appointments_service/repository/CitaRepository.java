package com.barberia.appointments_service.repository;

import com.barberia.appointments_service.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {

    List<Cita> findByIdBarberoAndFecha(Integer idBarbero, LocalDate fecha);

    List<Cita> findByIdCliente(Integer idCliente);

    List<Cita> findByEstadoInAndNotificacionProgramadaFalseAndHoraSalidaRecomendadaLessThanEqual(
            List<String> estados,
            LocalDateTime fechaHora
    );
}