package com.barberia.appointments_service.dto;

import java.time.LocalDateTime;

public record CalculoDesplazamientoResponse(
        Integer duracionViajeMinutos,
        LocalDateTime horaSalidaRecomendada
) {
}