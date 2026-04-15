package com.Logistica.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Excepción base personalizada para la aplicación
 */
@Getter
@AllArgsConstructor
public class LogisticaException extends RuntimeException {
    private String codigo;
    private String mensaje;
    private int statusCode;

    public LogisticaException(String codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.statusCode = 400;
    }
}
