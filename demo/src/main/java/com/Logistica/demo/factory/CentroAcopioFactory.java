package com.Logistica.demo.factory;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.Logistica.demo.dto.ResultadoValidacion;
import com.Logistica.demo.model.CentroAcopio;
import com.Logistica.demo.repository.CentroAcopioRepository;
import com.Logistica.demo.strategy.ValidadorCentroAcopio;

/**
 * Factory concreta para crear Centros de Acopio
 * Aplica validaciones y reglas de negocio específicas de centros
 * Implementa el Factory Pattern
 */
@Component
public class CentroAcopioFactory implements RecursoFactory<CentroAcopio> {

    @Autowired
    private CentroAcopioRepository centroAcopioRepository;

    @Autowired
    private ValidadorCentroAcopio validador;

    @Override
    public CentroAcopio crear(CentroAcopio centro) {
        // Aplicar validación
        ResultadoValidacion resultado = validador.validar(centro);
        if (!resultado.isValido()) {
            throw new IllegalArgumentException(
                String.format("Validación fallida: %s (Código: %s)", 
                    resultado.getMensaje(), 
                    resultado.getCodigoError())
            );
        }

        // Persistir
        return centroAcopioRepository.save(centro);
    }

    @Override
    public CentroAcopio actualizar(Long id, CentroAcopio detalles) {
        return centroAcopioRepository.findById(id)
            .map(centroExistente -> {
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
                centroExistente.setNombre(detalles.getNombre());
                centroExistente.setUbicacion(detalles.getUbicacion());
                centroExistente.setContacto(detalles.getContacto());
                centroExistente.setCapacidadMaxima(detalles.getCapacidadMaxima());

                return centroAcopioRepository.save(centroExistente);
            })
            .orElseThrow(() -> new IllegalArgumentException("Centro de acopio con ID " + id + " no encontrado"));
    }

    @Override
    public Optional<CentroAcopio> obtenerPorId(Long id) {
        return centroAcopioRepository.findById(id);
    }

    @Override
    public Class<CentroAcopio> getTipoEntidad() {
        return CentroAcopio.class;
    }
}
