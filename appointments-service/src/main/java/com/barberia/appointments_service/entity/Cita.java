package com.barberia.appointments_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "cita")
@Getter
@Setter
@NoArgsConstructor
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Integer idCita;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @Column(name = "tiempo_estimado_llegada")
    private LocalTime tiempoEstimadoLlegada;

    @Column(name = "estado")
    private String estado;

    @Column(name = "id_cliente", nullable = false)
    private Integer idCliente;

    @Column(name = "id_barbero", nullable = false)
    private Integer idBarbero;

    @Column(name = "duracion_viaje_minutos")
    private Integer duracionViajeMinutos;

    @Column(name = "hora_salida_recomendada")
    private LocalDateTime horaSalidaRecomendada;

    @Column(name = "notificacion_programada", nullable = false)
    private Boolean notificacionProgramada = false;
}