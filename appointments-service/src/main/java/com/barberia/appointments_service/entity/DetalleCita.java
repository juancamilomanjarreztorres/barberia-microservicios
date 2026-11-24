package com.barberia.appointments_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "detalle_cita")
@Getter
@Setter
@NoArgsConstructor
public class DetalleCita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer idDetalle;

    @Column(name = "precio", nullable = false)
    private BigDecimal precio;

    @Column(name = "id_cita", nullable = false)
    private Integer idCita;

    @Column(name = "id_servicio", nullable = false)
    private Integer idServicio;
}