package com.Logistica.demo.strategy;

import org.springframework.stereotype.Component;
import com.Logistica.demo.dto.ResultadoValidacion;
import com.Logistica.demo.model.CentroAcopio;

/**
 * Implementación de Strategy para validación de Centros de Acopio
 * Encapsula la lógica de validación específica de centros de acopio
 */
@Component
public class ValidadorCentroAcopio implements EstrategiaValidacion {

    @Override
    public ResultadoValidacion validar(Object recurso) {
        if (!(recurso instanceof CentroAcopio)) {
            return ResultadoValidacion.error("El recurso no es una instancia de CentroAcopio", "TYPE_ERROR");
        }

        CentroAcopio centro = (CentroAcopio) recurso;

        // Validar nombre
        if (centro.getNombre() == null || centro.getNombre().trim().isEmpty()) {
            return ResultadoValidacion.error("El nombre del centro es obligatorio", "NOMBRE_REQUIRED");
        }

        if (centro.getNombre().length() < 3 || centro.getNombre().length() > 100) {
            return ResultadoValidacion.error("El nombre debe tener entre 3 y 100 caracteres", "NOMBRE_LENGTH");
        }

        // Validar ubicación
        if (centro.getUbicacion() == null || centro.getUbicacion().trim().isEmpty()) {
            return ResultadoValidacion.error("La ubicación es obligatoria", "UBICACION_REQUIRED");
        }

        // Validar contacto
        if (centro.getContacto() == null || centro.getContacto().trim().isEmpty()) {
            return ResultadoValidacion.error("El contacto es obligatorio", "CONTACTO_REQUIRED");
        }

        if (!centro.getContacto().matches("\\d{8,15}")) {
            return ResultadoValidacion.error("El contacto debe ser un número válido (8-15 dígitos)", "CONTACTO_FORMAT");
        }

        // Validar capacidad máxima
        if (centro.getCapacidadMaxima() == null || centro.getCapacidadMaxima().trim().isEmpty()) {
            return ResultadoValidacion.error("La capacidad máxima es obligatoria", "CAPACIDAD_REQUIRED");
        }

        try {
            Double capacidad = Double.parseDouble(centro.getCapacidadMaxima());
            if (capacidad <= 0) {
                return ResultadoValidacion.error(
                    "La capacidad máxima debe ser mayor a 0",
                    "CAPACIDAD_INVALID"
                );
            }
        } catch (NumberFormatException e) {
            return ResultadoValidacion.error(
                "La capacidad máxima debe ser un número válido",
                "CAPACIDAD_FORMAT"
            );
        }

        return ResultadoValidacion.exitoso("Centro de acopio validado exitosamente");
    }

    @Override
    public Class<?> getTipoEntidad() {
        return CentroAcopio.class;
    }
}
