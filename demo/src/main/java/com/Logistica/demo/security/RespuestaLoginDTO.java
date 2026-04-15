package com.Logistica.demo.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuesta de login (PASO 1 - Generar código 2FA)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespuestaLoginDTO {
    private Boolean exitoso;
    private String mensaje;
    private Long usuarioId;
    private String email;
    private String nombre;
    private String rol;
    private String codigoTemporal; // Solo en desarrollo (en producción: NO enviar)
    private String instrucciones; // Texto con instrucciones
}
