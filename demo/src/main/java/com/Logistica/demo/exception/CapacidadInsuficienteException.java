package com.Logistica.demo.exception;

/**
 * Excepción cuando la capacidad es insuficiente
 */
public class CapacidadInsuficienteException extends LogisticaException {
    public CapacidadInsuficienteException(String mensaje) {
        super("CAPACIDAD_INSUFICIENTE", mensaje, 400);
    }

    public CapacidadInsuficienteException(String tipo, Double disponible, Double requerida) {
        super("CAPACIDAD_INSUFICIENTE", 
              String.format("%s - Disponible: %f, Requerido: %f", tipo, disponible, requerida), 
              400);
    }
}
