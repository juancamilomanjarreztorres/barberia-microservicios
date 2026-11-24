package com.barberia.appointments_service.service;

import com.barberia.appointments_service.entity.Cita;
import com.barberia.appointments_service.repository.CitaRepository;
import tools.jackson.databind.JsonNode;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.barberia.appointments_service.dto.CalculoDesplazamientoResponse;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final ClienteCatalogoService clienteService;
    private final OsrmService osrmService;

    public CitaService(
            CitaRepository citaRepository,
            ClienteCatalogoService clienteService,
            OsrmService osrmService) {

        this.citaRepository = citaRepository;
        this.clienteService = clienteService;
        this.osrmService = osrmService;
    }

    // Calcular duración del viaje y hora recomendada de salida
    private void calcularTiempoSalida(Cita cita) {

        try {
            JsonNode cliente = clienteService.obtenerCliente(
                    cita.getIdCliente()
            );

            if (cliente.path("latitud").isNull()
                    || cliente.path("longitud").isNull()) {
                throw new RuntimeException(
                        "El cliente no tiene coordenadas GPS registradas."
                );
            }

            BigDecimal latitud = cliente.get("latitud").decimalValue();
            BigDecimal longitud = cliente.get("longitud").decimalValue();

            JsonNode ruta = osrmService.calcularRuta(latitud, longitud);

            double duracionSegundos = ruta.path("duration").asDouble();

            int duracionMinutos = Math.max(
                    1,
                    (int) Math.ceil(duracionSegundos / 60.0)
            );

            LocalDateTime fechaHoraCita = LocalDateTime.of(
                    cita.getFecha(),
                    cita.getHora()
            );

            LocalDateTime horaSalida = fechaHoraCita.minusMinutes(
                    duracionMinutos
            );

            cita.setDuracionViajeMinutos(duracionMinutos);
            cita.setHoraSalidaRecomendada(horaSalida);
            cita.setNotificacionProgramada(false);

        } catch (Exception e) {
            throw new RuntimeException(
                    "No fue posible calcular el tiempo de desplazamiento: "
                            + e.getMessage(),
                    e
            );
        }
    }

    // Registrar una cita
    @Transactional
    public Cita registrar(Cita cita) {

        if (cita.getEstado() == null || cita.getEstado().isBlank()) {
            cita.setEstado("Pendiente");
        }

        calcularTiempoSalida(cita);

        return citaRepository.save(cita);
    }

    // Listar todas las citas
    public List<Cita> listar() {
        return citaRepository.findAll();
    }

    // Buscar una cita por ID
    public Optional<Cita> buscarPorId(Integer id) {
        return citaRepository.findById(id);
    }

    // Actualizar una cita
    @Transactional
    public Cita actualizar(Integer id, Cita datos) {

        Cita cita = citaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Cita no encontrada"));

        cita.setFecha(datos.getFecha());
        cita.setHora(datos.getHora());
        cita.setEstado(datos.getEstado());
        cita.setIdCliente(datos.getIdCliente());
        cita.setIdBarbero(datos.getIdBarbero());

        calcularTiempoSalida(cita);

        return citaRepository.save(cita);
    }

    // Cancelar una cita
    public void cancelar(Integer id) {

        Cita cita = citaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Cita no encontrada"));

        cita.setEstado("Cancelada");
        citaRepository.save(cita);
    }

    public CalculoDesplazamientoResponse calcularDesplazamiento(
            Integer idCliente,
            LocalDateTime fechaHoraCita) {

        Cita cita = new Cita();

        cita.setIdCliente(idCliente);
        cita.setFecha(fechaHoraCita.toLocalDate());
        cita.setHora(fechaHoraCita.toLocalTime());

        calcularTiempoSalida(cita);

        return new CalculoDesplazamientoResponse(
                cita.getDuracionViajeMinutos(),
                cita.getHoraSalidaRecomendada()
        );
    }
}