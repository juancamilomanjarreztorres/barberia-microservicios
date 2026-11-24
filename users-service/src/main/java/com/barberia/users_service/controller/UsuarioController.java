package com.barberia.users_service.controller;

import com.barberia.users_service.dto.UsuarioResponse;
import com.barberia.users_service.entity.Usuario;
import com.barberia.users_service.service.UsuarioService;
import com.barberia.users_service.dto.RegistroUsuarioRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import com.barberia.users_service.dto.LoginRequest;
import com.barberia.users_service.dto.LoginResponse;
import org.springframework.security.core.Authentication;
import com.barberia.users_service.dto.ActualizarPerfilRequest;
import com.barberia.users_service.dto.CambiarContrasenaRequest;
import com.barberia.users_service.dto.CambiarEstadoUsuarioRequest;
import com.barberia.users_service.dto.CambiarRolUsuarioRequest;
import com.barberia.users_service.dto.SolicitarRecuperacionRequest;
import com.barberia.users_service.dto.RestablecerContrasenaRequest;
import com.barberia.users_service.dto.RegistroUsuarioAdminRequest;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Consultar todos los usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {

        List<UsuarioResponse> usuarios = usuarioService.listarUsuarios()
                .stream()
                .map(this::convertirAResponse)
                .toList();

        return ResponseEntity.ok(usuarios);
    }

    // Consultar usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Integer id) {

        return usuarioService.buscarPorId(id)
                .map(this::convertirAResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Convertir entidad a respuesta pública
    private UsuarioResponse convertirAResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getUsuario(),
                usuario.getCorreo(),
                usuario.getRol(),
                usuario.getEstado()
        );
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> registrarUsuario(
            @Valid @RequestBody RegistroUsuarioRequest request) {

        UsuarioResponse usuario = usuarioService.registrarUsuario(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PostMapping("/admin")
    public ResponseEntity<UsuarioResponse> registrarUsuarioAdministrativo(
            @Valid @RequestBody RegistroUsuarioAdminRequest request) {

        UsuarioResponse respuesta =
                usuarioService.registrarUsuarioAdministrativo(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> iniciarSesion(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse respuesta = usuarioService.iniciarSesion(request);

        return ResponseEntity.ok(respuesta);
    }
    @GetMapping("/perfil")
    public ResponseEntity<UsuarioResponse> obtenerPerfil(Authentication authentication) {
        return usuarioService.buscarPorUsuario(authentication.getName())
                .map(this::convertirAResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/perfil")
    public ResponseEntity<UsuarioResponse> actualizarPerfil(
            @Valid @RequestBody ActualizarPerfilRequest request,
            Authentication authentication) {

        UsuarioResponse respuesta = usuarioService.actualizarPerfil(
                authentication.getName(),
                request
        );

        return ResponseEntity.ok(respuesta);
    }
    @PutMapping("/cambiar-contrasena")
    public ResponseEntity<Void> cambiarContrasena(
            @Valid @RequestBody CambiarContrasenaRequest request,
            Authentication authentication) {

        usuarioService.cambiarContrasena(
                authentication.getName(),
                request
        );

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<UsuarioResponse> cambiarEstadoUsuario(
            @PathVariable Integer id,
            @Valid @RequestBody CambiarEstadoUsuarioRequest request) {

        UsuarioResponse respuesta =
                usuarioService.cambiarEstadoUsuario(id, request);

        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/{id}/rol")
    public ResponseEntity<UsuarioResponse> cambiarRolUsuario(
            @PathVariable Integer id,
            @Valid @RequestBody CambiarRolUsuarioRequest request,
            Authentication authentication) {

        UsuarioResponse respuesta = usuarioService.cambiarRolUsuario(
                id,
                request,
                authentication.getName()
        );

        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(
            @PathVariable Integer id,
            Authentication authentication) {

        usuarioService.eliminarUsuario(id, authentication.getName());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/recuperar-contrasena")
    public ResponseEntity<String> solicitarRecuperacion(
            @Valid @RequestBody SolicitarRecuperacionRequest request) {

        usuarioService.solicitarRecuperacion(request.correo());

        return ResponseEntity.ok(
                "Si el correo está registrado, recibirás instrucciones para recuperar tu contraseña."
        );
    }

    @PostMapping("/restablecer-contrasena")
    public ResponseEntity<String> restablecerContrasena(
            @Valid @RequestBody RestablecerContrasenaRequest request) {

        usuarioService.restablecerContrasena(request);

        return ResponseEntity.ok("Contraseña restablecida correctamente");
    }




}
