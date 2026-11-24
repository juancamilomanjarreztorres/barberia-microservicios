package com.barberia.appointments_service.service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ClienteCatalogoService {

    private static final String URL_CATALOGO =
            System.getenv().getOrDefault(
                    "CATALOGO_SERVICE_URL",
                    "http://localhost:8082"
            ) + "/api/clientes/";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ClienteCatalogoService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newHttpClient();
    }

    public JsonNode obtenerCliente(Integer idCliente) throws Exception {

        String url = URL_CATALOGO + idCliente;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new RuntimeException(
                    "No se pudo obtener el cliente. Código: "
                            + response.statusCode()
            );
        }

        return objectMapper.readTree(response.body());
    }
}