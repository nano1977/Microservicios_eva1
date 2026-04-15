package com.Logistica.demo.strategy;

import org.springframework.stereotype.Component;
import com.Logistica.demo.dto.ResultadoValidacion;
import com.Logistica.demo.model.Inventario;

/**
 * Implementación de Strategy para validación de Inventario
 * Encapsula la lógica de validación específica de items de inventario
 */
@Component
public class ValidadorInventario implements EstrategiaValidacion {

    @Override
    public ResultadoValidacion validar(Object recurso) {
        if (!(recurso instanceof Inventario)) {
            return ResultadoValidacion.error("El recurso no es una instancia de Inventario", "TYPE_ERROR");
        }

        Inventario inventario = (Inventario) recurso;

        // Validar recurso
        if (inventario.getRecurso() == null || inventario.getRecurso().trim().isEmpty()) {
            return ResultadoValidacion.error("El tipo de recurso es obligatorio", "RECURSO_REQUIRED");
        }

        // Validar que sea un recurso conocido
        if (!inventario.getRecurso().matches("(Alimento|Ropa|Insumos Médicos|Otros)")) {
            return ResultadoValidacion.error(
                "El recurso debe ser: Alimento, Ropa, Insumos Médicos u Otros",
                "RECURSO_INVALID"
            );
        }

        // Validar cantidad
        if (inventario.getCantidad() == null || inventario.getCantidad() <= 0) {
            return ResultadoValidacion.error("La cantidad debe ser mayor a 0", "CANTIDAD_INVALID");
        }

        if (inventario.getCantidad() > 1000000) {
            return ResultadoValidacion.error("La cantidad no puede exceder 1,000,000", "CANTIDAD_MAX");
        }

        // Validar unidad de medida
        if (inventario.getUnidadMedida() == null || inventario.getUnidadMedida().trim().isEmpty()) {
            return ResultadoValidacion.error("La unidad de medida es obligatoria", "UNIDAD_REQUIRED");
        }

        if (!inventario.getUnidadMedida().matches("(Kilos|Unidades|Cajas|Litros|Paquetes)")) {
            return ResultadoValidacion.error(
                "La unidad debe ser: Kilos, Unidades, Cajas, Litros o Paquetes",
                "UNIDAD_INVALID"
            );
        }

        // Validar centro de acopio
        if (inventario.getCentroAcopio() == null || inventario.getCentroAcopio().getId() == null) {
            return ResultadoValidacion.error(
                "El centro de acopio es obligatorio",
                "CENTRO_REQUIRED"
            );
        }

        return ResultadoValidacion.exitoso("Inventario validado exitosamente");
    }

    @Override
    public Class<?> getTipoEntidad() {
        return Inventario.class;
    }
}
