package com.Logistica.demo.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitud de login (PASO 1)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudLoginDTO {
    private String email;
    private String contraseña;
    
    @Override
    public String toString() {
        return String.format("Login[%s]", email);
    }
}
