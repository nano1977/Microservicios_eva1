package com.Logistica.demo.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para validación de código 2FA (PASO 3)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidacionCodigoDTO {
    private Long usuarioId;
    private String codigoIngresado; // Código de 6 dígitos que el usuario ingresa
    
    @Override
    public String toString() {
        return String.format("Validacion2FA[usuario:%d, codigo:***]", usuarioId);
    }
}
