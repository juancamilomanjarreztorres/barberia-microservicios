package com.barberia.catalog_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AdministradorRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100)
        String nombre,

        @Size(max = 20)
        String nivelAcceso,

        @Size(max = 50)
        String area,

        @NotNull(message = "El identificador del usuario es obligatorio")
        Integer idUsuario

) {}
