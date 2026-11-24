package com.barberia.users_service.service;

import com.barberia.users_service.dto.RegistroUsuarioRequest;
import com.barberia.users_service.dto.UsuarioResponse;
import com.barberia.users_service.entity.Usuario;
import com.barberia.users_service.repository.UsuarioRepository;
import com.barberia.users_service.dto.ActualizarPerfilRequest;
import com.barberia.users_service.dto.CambiarContrasenaRequest;
import com.barberia.users_service.dto.CambiarEstadoUsuarioRequest;
import com.barberia.users_service.dto.CambiarRolUsuarioRequest;
import com.barberia.users_service.dto.RestablecerContrasenaRequest;
import com.barberia.users_service.dto.RegistroUsuarioAdminRequest;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.barberia.users_service.dto.LoginRequest;
import com.barberia.users_service.dto.LoginResponse;
import com.barberia.users_service.security.JwtService;
import java.security.SecureRandom;
import java.util.HexFormat;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder, JwtService jwtService, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;

    }

    public UsuarioResponse registrarUsuario(RegistroUsuarioRequest request) {

        if (usuarioRepository.existsByUsuario(request.usuario())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El nombre de usuario ya está registrado"
            );
        }
        if (usuarioRepository.existsByCorreo(request.correo())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El correo electrónico ya está registrado"
            );
        }

        Usuario usuario = new Usuario();

        usuario.setUsuario(request.usuario());
        usuario.setCorreo(request.correo());
        usuario.setContrasena(passwordEncoder.encode(request.contrasena()));
        usuario.setRol("Cliente");
        usuario.setEstado("Activo");

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return new UsuarioResponse(
                usuarioGuardado.getIdUsuario(),
                usuarioGuardado.getUsuario(),
                usuarioGuardado.getCorreo(),
                usuarioGuardado.getRol(),
                usuarioGuardado.getEstado()
        );
    }

    public UsuarioResponse registrarUsuarioAdministrativo(RegistroUsuarioAdminRequest request) {

        if (usuarioRepository.existsByUsuario(request.usuario())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El nombre de usuario ya está registrado"
            );
        }

        if (usuarioRepository.existsByCorreo(request.correo())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El correo electrónico ya está registrado"
            );
        }

        Usuario usuario = new Usuario();

        usuario.setUsuario(request.usuario());
        usuario.setCorreo(request.correo());
        usuario.setContrasena(passwordEncoder.encode(request.contrasena()));
        usuario.setRol(request.rol());
        usuario.setEstado("Activo");

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return new UsuarioResponse(
                usuarioGuardado.getIdUsuario(),
                usuarioGuardado.getUsuario(),
                usuarioGuardado.getCorreo(),
                usuarioGuardado.getRol(),
                usuarioGuardado.getEstado()
        );
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorUsuario(String usuario) {
        return usuarioRepository.findByUsuario(usuario);
    }

    public UsuarioResponse actualizarPerfil(
            String usuarioActual,
            ActualizarPerfilRequest request) {

        Usuario usuario = usuarioRepository.findByUsuario(usuarioActual)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (!usuarioActual.equals(request.usuario())
                && usuarioRepository.existsByUsuario(request.usuario())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El nombre de usuario ya está registrado");
        }

        usuario.setUsuario(request.usuario());

        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        return new UsuarioResponse(
                usuarioActualizado.getIdUsuario(),
                usuarioActualizado.getCorreo(),
                usuarioActualizado.getUsuario(),
                usuarioActualizado.getRol(),
                usuarioActualizado.getEstado()
        );
    }

    public void eliminarUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }
    public LoginResponse iniciarSesion(LoginRequest request) {

        Usuario usuario = usuarioRepository.findByUsuario(request.usuario())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Usuario o contraseña incorrectos"
                ));

        if (!passwordEncoder.matches(
                request.contrasena(),
                usuario.getContrasena())) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuario o contraseña incorrectos"
            );
        }

        if (!"Activo".equalsIgnoreCase(usuario.getEstado())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "La cuenta está inactiva"
            );
        }

        String token = jwtService.generateToken(usuario.getUsuario());

        return new LoginResponse(
                token,
                usuario.getIdUsuario(),
                usuario.getUsuario(),
                usuario.getRol()
        );
    }
    public void cambiarContrasena(
            String username,
            CambiarContrasenaRequest request) {

        Usuario usuario = usuarioRepository.findByUsuario(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (!passwordEncoder.matches(
                request.contrasenaActual(), usuario.getContrasena())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La contraseña actual es incorrecta");
        }

        if (passwordEncoder.matches(
                request.nuevaContrasena(), usuario.getContrasena())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La nueva contraseña debe ser diferente a la actual");
        }

        usuario.setContrasena(
                passwordEncoder.encode(request.nuevaContrasena()));

        usuarioRepository.save(usuario);
    }
    public UsuarioResponse cambiarEstadoUsuario(
            Integer id,
            CambiarEstadoUsuarioRequest request) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        usuario.setEstado(request.estado());

        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        return new UsuarioResponse(
                usuarioActualizado.getIdUsuario(),
                usuarioActualizado.getCorreo(),
                usuarioActualizado.getUsuario(),
                usuarioActualizado.getRol(),
                usuarioActualizado.getEstado()
        );
    }

    public UsuarioResponse cambiarRolUsuario(
            Integer id,
            CambiarRolUsuarioRequest request,
            String usuarioActual) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"
                ));

        if (usuario.getUsuario().equals(usuarioActual)
                && !request.rol().equals("Administrador")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No puedes quitarte tu propio rol de administrador"
            );
        }

        usuario.setRol(request.rol());

        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        return new UsuarioResponse(
                usuarioActualizado.getIdUsuario(),
                usuarioActualizado.getCorreo(),
                usuarioActualizado.getUsuario(),
                usuarioActualizado.getRol(),
                usuarioActualizado.getEstado()
        );
    }
    public void eliminarUsuario(Integer id, String usuarioActual) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"
                ));

        if (usuario.getUsuario().equals(usuarioActual)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No puedes eliminar tu propia cuenta"
            );
        }

        usuarioRepository.delete(usuario);
    }
    public void solicitarRecuperacion(String correo) {

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe una cuenta asociada a ese correo"
                ));

        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);

        String token = HexFormat.of().formatHex(bytes);

        usuario.setTokenRecuperacion(token);
        usuario.setTokenExpiracion(LocalDateTime.now().plusMinutes(15));

        usuarioRepository.save(usuario);

        String enlace = "http://localhost/Login-Register/assets/php/cambiar_password.php?token=" + token;

        emailService.enviarCorreoRecuperacion(correo, enlace);
    }

    public void restablecerContrasena(RestablecerContrasenaRequest request) {

        Usuario usuario = usuarioRepository.findByTokenRecuperacion(request.token())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "El token no es válido"
                ));

        if (usuario.getTokenExpiracion() == null ||
                usuario.getTokenExpiracion().isBefore(LocalDateTime.now())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El enlace ha expirado"
            );
        }

        if (!request.nuevaContrasena().equals(request.confirmarContrasena())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Las contraseñas no coinciden"
            );
        }

        usuario.setContrasena(
                passwordEncoder.encode(request.nuevaContrasena())
        );

        usuario.setTokenRecuperacion(null);
        usuario.setTokenExpiracion(null);

        usuarioRepository.save(usuario);
    }


}