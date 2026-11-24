package com.barberia.users_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistroUsuarioAdminRequest(

        @NotBlank(message = "El usuario es obligatorio")
        @Size(max = 100)
        String usuario,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no tiene un formato válido")
        @Size(max = 150)
        String correo,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 72,
                message = "La contraseña debe tener entre 8 y 72 caracteres")
        String contrasena,

        @NotBlank(message = "El rol es obligatorio")
        @Pattern(
                regexp = "Cliente|Barbero|Administrador",
                message = "El rol debe ser Cliente, Barbero o Administrador"
        )
        String rol

) {}