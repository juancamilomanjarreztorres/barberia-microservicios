package com.barberia.users_service.dto;

public record UsuarioResponse(
        Integer idUsuario,
        String usuario,
        String correo,
        String rol,
        String estado
) {}