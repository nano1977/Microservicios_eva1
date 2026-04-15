package com.Logistica.demo.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio para usuarios
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Obtiene usuario por email
     */
    Optional<Usuario> findByEmail(String email);
    
    /**
     * Verifica si email existe
     */
    boolean existsByEmail(String email);
    
    /**
     * Obtiene usuarios por rol
     */
    Iterable<Usuario> findByRol(String rol);
    
    /**
     * Obtiene usuarios activos
     */
    Iterable<Usuario> findByActivo(Boolean activo);
}
