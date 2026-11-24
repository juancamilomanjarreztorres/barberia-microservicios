package com.barberia.appointments_service.service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class OsrmService {

    private static final double LATITUD_BARBERIA = 10.535451;
    private static final double LONGITUD_BARBERIA = -75.397941;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OsrmService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newHttpClient();
    }

    public JsonNode calcularRuta(BigDecimal latitudCliente, BigDecimal longitudCliente)
            throws Exception {

        String url = "https://router.project-osrm.org/route/v1/driving/"
                + longitudCliente + "," + latitudCliente + ";"
                + LONGITUD_BARBERIA + "," + LATITUD_BARBERIA
                + "?overview=false";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new RuntimeException("Error al consultar OSRM: "
                    + response.statusCode());
        }

        JsonNode resultado = objectMapper.readTree(response.body());

        if (!"Ok".equals(resultado.path("code").asText())) {
            throw new RuntimeException("No se pudo calcular la ruta.");
        }

        return resultado.path("routes").get(0);
    }
}