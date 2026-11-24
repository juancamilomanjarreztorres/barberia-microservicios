package com.barberia.users_service.repository;

import com.barberia.users_service.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    boolean existsByUsuario(String usuario);
    Optional<Usuario> findByUsuario(String usuario);
    boolean existsByCorreo(String correo);
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByTokenRecuperacion(String tokenRecuperacion);
}