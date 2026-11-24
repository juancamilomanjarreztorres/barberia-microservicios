package com.barberia.appointments_service.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Service
public class ServicioImagenService {

    private final Cloudinary cloudinary = new Cloudinary();

    private static final Set<String> EXTENSIONES_PERMITIDAS =
            Set.of(".jpg", ".jpeg", ".png", ".webp");

    public String guardar(MultipartFile archivo) {

        if (archivo == null || archivo.isEmpty()) {
            throw new RuntimeException("Debes seleccionar una imagen.");
        }

        String nombreOriginal = archivo.getOriginalFilename();

        if (nombreOriginal == null || !nombreOriginal.contains(".")) {
            throw new RuntimeException(
                    "El archivo no tiene una extensión válida."
            );
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
            Map resultado = cloudinary.uploader().upload(
                    archivo.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "barberia/servicios",
                            "resource_type", "image"
                    )
            );

            return resultado.get("secure_url").toString();

        } catch (IOException e) {
            throw new RuntimeException(
                    "No se pudo subir la imagen a Cloudinary.",
                    e
            );
        }
    }
}