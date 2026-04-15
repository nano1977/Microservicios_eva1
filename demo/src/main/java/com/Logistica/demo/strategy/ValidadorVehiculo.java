package com.Logistica.demo.strategy;

import org.springframework.stereotype.Component;

import com.Logistica.demo.dto.ResultadoValidacion;
import com.Logistica.demo.model.Vehiculo;

/**
 * Implementación de Strategy para validación de Vehículos
 * Encapsula la lógica de validación específica de vehículos
 */
@Component
public class ValidadorVehiculo implements EstrategiaValidacion {

    @Override
    public ResultadoValidacion validar(Object recurso) {
        if (!(recurso instanceof Vehiculo)) {
            return ResultadoValidacion.error("El recurso no es una instancia de Vehiculo", "TYPE_ERROR");
        }

        Vehiculo vehiculo = (Vehiculo) recurso;

        // Validar patente
        if (vehiculo.getPatente() == null || vehiculo.getPatente().trim().isEmpty()) {
            return ResultadoValidacion.error("La patente es obligatoria", "PATENTE_REQUIRED");
        }

        if (vehiculo.getPatente().length() < 3 || vehiculo.getPatente().length() > 10) {
            return ResultadoValidacion.error("La patente debe tener entre 3 y 10 caracteres", "PATENTE_LENGTH");
        }

        // Validar modelo
        if (vehiculo.getModelo() == null || vehiculo.getModelo().trim().isEmpty()) {
            return ResultadoValidacion.error("El modelo es obligatorio", "MODELO_REQUIRED");
        }

        // Validar chofer
        if (vehiculo.getChofer() == null || vehiculo.getChofer().trim().isEmpty()) {
            return ResultadoValidacion.error("El nombre del chofer es obligatorio", "CHOFER_REQUIRED");
        }

        // Validar capacidad de carga
        if (vehiculo.getCapacidadCarga() == null || vehiculo.getCapacidadCarga() <= 0) {
            return ResultadoValidacion.error("La capacidad de carga debe ser mayor a 0", "CAPACIDAD_INVALID");
        }

        if (vehiculo.getCapacidadCarga() > 100000) {
            return ResultadoValidacion.error("La capacidad de carga no puede exceder 100,000 kg", "CAPACIDAD_MAX");
        }

        // Validar estado
        String estado = vehiculo.getEstado() != null ? vehiculo.getEstado() : "Disponible";
        if (!estado.matches("(Disponible|En Ruta|Mantenimiento)")) {
            return ResultadoValidacion.error(
                "El estado debe ser: Disponible, En Ruta o Mantenimiento",
                "ESTADO_INVALID"
            );
        }

        return ResultadoValidacion.exitoso("Vehículo validado exitosamente");
    }

    @Override
    public Class<?> getTipoEntidad() {
        return Vehiculo.class;
    }
}
