package com.Logistica.demo.strategy;

import com.Logistica.demo.dto.ResultadoValidacion;

/**
 * Interfaz Strategy para validación de entidades
 * Define el contrato para diferentes estrategias de validación
 * según el tipo de recurso (Vehículo, CentroAcopio, Inventario)
 */
public interface EstrategiaValidacion {
    /**
     * Valida un objeto según las reglas específicas de la estrategia
     * 
     * @param recurso el objeto a validar
     * @return ResultadoValidacion con el estado y mensaje
     */
    ResultadoValidacion validar(Object recurso);

    /**
     * Retorna el tipo de entidad que valida esta estrategia
     */
    Class<?> getTipoEntidad();
}
