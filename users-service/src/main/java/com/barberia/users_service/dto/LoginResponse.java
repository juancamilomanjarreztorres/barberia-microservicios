package com.barberia.users_service.dto;

public record LoginResponse(
        String token,
        Integer idUsuario,
        String usuario,
        String rol
) {}