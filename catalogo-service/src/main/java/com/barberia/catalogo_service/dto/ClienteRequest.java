package com.barberia.catalog_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ClienteRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100)
        String nombre,

        @NotBlank(message = "El teléfono es obligatorio")
        @Size(max = 15)
        String telefono,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no es válido")
        @Size(max = 100)
        String correo,

        @NotBlank(message = "La ubicación es obligatoria")
        String ubicacionActual,

        BigDecimal latitud,

        BigDecimal longitud,

        @NotNull(message = "El identificador del usuario es obligatorio")
        Integer idUsuario

) {}