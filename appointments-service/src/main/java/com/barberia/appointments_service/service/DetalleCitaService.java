package com.barberia.appointments_service.service;

import com.barberia.appointments_service.entity.DetalleCita;
import com.barberia.appointments_service.repository.DetalleCitaRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleCitaService {

    private final DetalleCitaRepository detalleCitaRepository;

    public DetalleCitaService(DetalleCitaRepository detalleCitaRepository) {
        this.detalleCitaRepository = detalleCitaRepository;
    }

    // Registrar detalle de una cita
    public DetalleCita registrar(DetalleCita detalle) {
        return detalleCitaRepository.save(detalle);
    }

    // Consultar detalles de una cita
    public List<DetalleCita> listarPorCita(Integer idCita) {
        return detalleCitaRepository.findByIdCita(idCita);
    }
}