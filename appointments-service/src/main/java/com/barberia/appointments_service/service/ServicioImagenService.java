package com.barberia.appointments_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.Set;

@Service
public class ServicioImagenService {

    @Value("${app.upload.dir}")
    private String directorioImagenes;

    private static final Set<String> EXTENSIONES_PERMITIDAS =
            Set.of(".jpg", ".jpeg", ".png", ".webp");

    public String guardar(MultipartFile archivo) {

        if (archivo == null || archivo.isEmpty()) {
            throw new RuntimeException("Debes seleccionar una imagen.");
        }

        String nombreOriginal = archivo.getOriginalFilename();

        if (nombreOriginal == null || !nombreOriginal.contains(".")) {
            throw new RuntimeException("El archivo no tiene una extensión válida.");
        }

        String extension = nombreOriginal.substring(
                nombreOriginal.lastIndexOf(".")
        ).toLowerCase();

        if (!EXTENSIONES_PERMITIDAS.contains(extension)) {
            throw new RuntimeException(
                    "Solo se permiten imágenes JPG, JPEG, PNG o WEBP."
            );
        }

        try {
            Path directorio = Paths.get(directorioImagenes)
                    .toAbsolutePath()
                    .normalize();

            Files.createDirectories(directorio);

            String nombreArchivo = UUID.randomUUID() + extension;

            Path destino = directorio.resolve(nombreArchivo).normalize();

            if (!destino.startsWith(directorio)) {
                throw new RuntimeException("Ruta de archivo no válida.");
            }

            Files.copy(archivo.getInputStream(), destino);

            return "/uploads/servicios/" + nombreArchivo;

        } catch (IOException e) {
            throw new RuntimeException(
                    "No se pudo guardar la imagen.",
                    e
            );
        }
    }
}