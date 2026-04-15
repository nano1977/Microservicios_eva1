package com.Logistica.demo.exception;

/**
 * Excepción de autenticación y autorización
 */
public class AutenticacionException extends LogisticaException {
    public AutenticacionException(String mensaje) {
        super("AUTENTICACION_ERROR", mensaje, 401);
    }

    public AutenticacionException(String codigo, String mensaje) {
        super(codigo, mensaje, 401);
    }
}
