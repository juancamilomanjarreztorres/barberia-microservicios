package com.barberia.users_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ActualizarPerfilRequest(

        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(max = 100, message = "El usuario no puede superar los 100 caracteres")
        String usuario

) {}