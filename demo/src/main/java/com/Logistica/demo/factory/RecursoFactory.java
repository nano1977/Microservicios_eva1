package com.Logistica.demo.factory;

import java.util.Optional;

/**
 * Interfaz Factory para crear recursos
 * Define el contrato para diferentes factories según el tipo de entidad
 * Implementa el Factory Pattern para centralizar la lógica de creación
 */
public interface RecursoFactory<T> {
    /**
     * Crea una nueva entidad aplicando reglas de negocio y validaciones
     * 
     * @param entidad la entidad a crear
     * @return la entidad creada y persistida
     * @throws IllegalArgumentException si la validación falla
     */
    T crear(T entidad);

    /**
     * Actualiza una entidad existente
     * 
     * @param id el identificador de la entidad
     * @param entidad los nuevos datos
     * @return la entidad actualizada
     */
    T actualizar(Long id, T entidad);

    /**
     * Obtiene una entidad por su identificador
     * 
     * @param id el identificador
     * @return Optional con la entidad si existe
     */
    Optional<T> obtenerPorId(Long id);

    /**
     * Retorna el tipo de entidad que gestiona esta factory
     */
    Class<T> getTipoEntidad();
}
