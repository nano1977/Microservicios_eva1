package com.Logistica.demo.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuesta de autenticación
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespuestaAutenticacionDTO {
    private Boolean exitoso;
    private String mensaje;
    private Long usuarioId;
    private String email;
    private String nombre;
    private String rol;
    private String token; // JWT token de sesión
}
