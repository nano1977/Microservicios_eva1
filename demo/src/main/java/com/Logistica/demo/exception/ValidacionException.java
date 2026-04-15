package com.Logistica.demo.exception;

/**
 * Excepción cuando la validación falla
 */
public class ValidacionException extends LogisticaException {
    public ValidacionException(String mensaje) {
        super("VALIDACION_ERROR", mensaje, 400);
    }

    public ValidacionException(String codigo, String mensaje) {
        super(codigo, mensaje, 400);
    }
}
