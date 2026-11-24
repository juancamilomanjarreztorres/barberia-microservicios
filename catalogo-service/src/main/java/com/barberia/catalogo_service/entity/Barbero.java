package com.barberia.catalog_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "barbero")
public class Barbero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_barbero")
    private Integer idBarbero;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "especialidad", length = 100)
    private String especialidad;

    @Column(name = "telefono", nullable = false, length = 15)
    private String telefono;

    @Column(name = "estado", length = 20)
    private String estado = "Activo";

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    public Integer getIdBarbero() {
        return idBarbero;
    }

    public void setIdBarbero(Integer idBarbero) {
        this.idBarbero = idBarbero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
}