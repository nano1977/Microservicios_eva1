package com.Logistica.demo.exception;

/**
 * Excepción cuando un recurso no es encontrado
 */
public class RecursoNoEncontradoException extends LogisticaException {
    public RecursoNoEncontradoException(String tipo, Long id) {
        super("RECURSO_NO_ENCONTRADO", 
              String.format("%s con ID %d no encontrado", tipo, id), 
              404);
    }

    public RecursoNoEncontradoException(String mensaje) {
        super("RECURSO_NO_ENCONTRADO", mensaje, 404);
    }
}
