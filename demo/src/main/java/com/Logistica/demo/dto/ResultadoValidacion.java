package com.Logistica.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa el resultado de una validación
 * Implementa el patrón Strategy para encapsular respuestas de validación
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoValidacion {
    private boolean valido;
    private String mensaje;
    private String codigoError;

    public ResultadoValidacion(boolean valido, String mensaje) {
        this.valido = valido;
        this.mensaje = mensaje;
        this.codigoError = valido ? "OK" : "VALIDATION_ERROR";
    }

    public static ResultadoValidacion exitoso(String mensaje) {
        return new ResultadoValidacion(true, mensaje, "OK");
    }

    public static ResultadoValidacion error(String mensaje, String codigo) {
        return new ResultadoValidacion(false, mensaje, codigo);
    }

    public static ResultadoValidacion error(String mensaje) {
        return error(mensaje, "VALIDATION_ERROR");
    }
}
