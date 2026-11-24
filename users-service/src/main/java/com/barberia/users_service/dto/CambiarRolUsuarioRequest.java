package com.barberia.users_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CambiarRolUsuarioRequest(

        @NotBlank(message = "El rol es obligatorio")
        @Pattern(
                regexp = "Cliente|Barbero|Administrador",
                message = "El rol debe ser Cliente, Barbero o Administrador"
        )
        String rol

) {}