package com.barberia.appointments_service.service;

import com.barberia.appointments_service.entity.Horario;
import com.barberia.appointments_service.repository.HorarioRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HorarioService {

    private final HorarioRepository horarioRepository;

    public HorarioService(HorarioRepository horarioRepository) {
        this.horarioRepository = horarioRepository;
    }

    // Registrar un horario
    public Horario registrar(Horario horario) {
        return horarioRepository.save(horario);
    }

    // Listar todos los horarios
    public List<Horario> listar() {
        return horarioRepository.findAll();
    }

    // Consultar horarios de un barbero
    public List<Horario> listarPorBarbero(Integer idBarbero) {
        return horarioRepository.findByIdBarbero(idBarbero);
    }

    // Consultar horarios de un barbero por día
    public List<Horario> listarPorBarberoYDia(Integer idBarbero, String dia) {
        return horarioRepository.findByIdBarberoAndDia(idBarbero, dia);
    }

    // Eliminar un horario
    public void eliminar(Integer id) {
        horarioRepository.deleteById(id);
    }
}
