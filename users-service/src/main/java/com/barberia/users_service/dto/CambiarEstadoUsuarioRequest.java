package com.barberia.users_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CambiarEstadoUsuarioRequest(

        @NotBlank(message = "El estado es obligatorio")
        @Pattern(
                regexp = "Activo|Inactivo",
                message = "El estado debe ser Activo o Inactivo"
        )
        String estado

) {}