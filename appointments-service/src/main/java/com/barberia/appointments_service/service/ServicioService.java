package com.barberia.appointments_service.service;

import com.barberia.appointments_service.entity.Servicio;
import com.barberia.appointments_service.repository.ServicioRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioService {

    private final ServicioRepository servicioRepository;

    public ServicioService(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    // Registrar servicio
    public Servicio registrar(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    // Listar servicios
    public List<Servicio> listar() {
        return servicioRepository.findAll();
    }

    // Buscar servicio por ID
    public Servicio buscarPorId(Integer id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
    }

    // Actualizar servicio
    public Servicio actualizar(Integer id, Servicio datos) {
        Servicio servicio = buscarPorId(id);

        servicio.setNombre(datos.getNombre());
        servicio.setPrecio(datos.getPrecio());
        servicio.setDuracion(datos.getDuracion());
        if (datos.getImagen() != null) {
            servicio.setImagen(datos.getImagen());
        }

        return servicioRepository.save(servicio);
    }

    // Eliminar servicio
    public void eliminar(Integer id) {
        servicioRepository.deleteById(id);
    }

    // Actualizar únicamente la imagen del servicio
    public Servicio actualizarImagen(Integer id, String imagen) {
        Servicio servicio = buscarPorId(id);

        servicio.setImagen(imagen);

        return servicioRepository.save(servicio);
    }

}