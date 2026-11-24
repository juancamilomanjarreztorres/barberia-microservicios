package com.barberia.appointments_service.repository;

import com.barberia.appointments_service.entity.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Integer> {

    List<Horario> findByIdBarbero(Integer idBarbero);

    List<Horario> findByIdBarberoAndDia(Integer idBarbero, String dia);
}
