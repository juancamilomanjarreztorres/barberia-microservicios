package com.barberia.appointments_service.service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class OsrmService {

    private static final double LATITUD_BARBERIA = 10.535451;
    private static final double LONGITUD_BARBERIA = -75.397941;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OsrmService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    public JsonNode calcularRuta(
            BigDecimal latitudCliente,
            BigDecimal longitudCliente) throws Exception {

        String url = "https://router.project-osrm.org/route/v1/driving/"
                + longitudCliente + "," + latitudCliente + ";"
                + LONGITUD_BARBERIA + "," + LATITUD_BARBERIA
                + "?overview=false";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .header("User-Agent", "BarberiaMT/1.0")
                .GET()
                .build();

        HttpResponse<String> response;

        try {
            response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        } catch (Exception e) {
            throw new RuntimeException(
                    "No fue posible establecer conexión con el servicio de rutas OSRM: "
                            + e.getClass().getSimpleName() + " - " + e.getMessage(),
                    e
            );
        }

        if (response.statusCode() != 200) {
            throw new RuntimeException(
                    "Error al consultar OSRM. Código HTTP: "
                            + response.statusCode()
            );
        }

        JsonNode resultado = objectMapper.readTree(response.body());

        if (!"Ok".equals(resultado.path("code").asText())) {
            throw new RuntimeException(
                    "OSRM no pudo calcular la ruta: "
                            + resultado.path("code").asText()
            );
        }

        JsonNode rutas = resultado.path("routes");

        if (!rutas.isArray() || rutas.isEmpty()) {
            throw new RuntimeException(
                    "OSRM no devolvió ninguna ruta disponible."
            );
        }

        return rutas.get(0);
    }
}
