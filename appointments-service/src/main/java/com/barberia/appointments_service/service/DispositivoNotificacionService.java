package com.barberia.appointments_service.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DispositivoNotificacionService {

    private final JdbcTemplate jdbcTemplate;

    public DispositivoNotificacionService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> obtenerTokensCliente(Integer idCliente) {

        String sql = """
                SELECT dn.token
                FROM dispositivo_notificacion dn
                INNER JOIN cliente c
                    ON c.id_usuario = dn.id_usuario
                WHERE c.id_cliente = ?
                  AND dn.activo = 1
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getString("token"),
                idCliente
        );
    }
}