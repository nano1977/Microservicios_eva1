package com.Logistica.demo.factory;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.Logistica.demo.dto.ResultadoValidacion;
import com.Logistica.demo.model.Inventario;
import com.Logistica.demo.repository.InventarioRepository;
import com.Logistica.demo.strategy.ValidadorInventario;

/**
 * Factory concreta para crear Items de Inventario
 * Aplica validaciones y reglas de negocio específicas de inventario
 * Implementa el Factory Pattern
 */
@Component
public class InventarioFactory implements RecursoFactory<Inventario> {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private ValidadorInventario validador;

    @Override
    public Inventario crear(Inventario inventario) {
        // Aplicar validación
        ResultadoValidacion resultado = validador.validar(inventario);
        if (!resultado.isValido()) {
            throw new IllegalArgumentException(
                String.format("Validación fallida: %s (Código: %s)", 
                    resultado.getMensaje(), 
                    resultado.getCodigoError())
            );
        }

        // Persistir
        return inventarioRepository.save(inventario);
    }

    @Override
    public Inventario actualizar(Long id, Inventario detalles) {
        return inventarioRepository.findById(id)
            .map(inventarioExistente -> {
                // Validar detalles
                ResultadoValidacion resultado = validador.validar(detalles);
                if (!resultado.isValido()) {
                    throw new IllegalArgumentException(
                        String.format("Validación fallida: %s (Código: %s)", 
                            resultado.getMensaje(), 
                            resultado.getCodigoError())
                    );
                }

                // Actualizar campos
                inventarioExistente.setRecurso(detalles.getRecurso());
                inventarioExistente.setCantidad(detalles.getCantidad());
                inventarioExistente.setUnidadMedida(detalles.getUnidadMedida());
                inventarioExistente.setCentroAcopio(detalles.getCentroAcopio());

                return inventarioRepository.save(inventarioExistente);
            })
            .orElseThrow(() -> new IllegalArgumentException("Inventario con ID " + id + " no encontrado"));
    }

    @Override
    public Optional<Inventario> obtenerPorId(Long id) {
        return inventarioRepository.findById(id);
    }

    @Override
    public Class<Inventario> getTipoEntidad() {
        return Inventario.class;
    }
}
