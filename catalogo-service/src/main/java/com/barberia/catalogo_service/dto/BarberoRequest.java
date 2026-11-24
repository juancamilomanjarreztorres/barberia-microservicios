package com.barberia.catalog_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BarberoRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100)
        String nombre,

        @Size(max = 100)
        String especialidad,

        @NotBlank(message = "El teléfono es obligatorio")
        @Size(max = 15)
        String telefono,

        @NotNull(message = "El identificador del usuario es obligatorio")
        Integer idUsuario

) {}