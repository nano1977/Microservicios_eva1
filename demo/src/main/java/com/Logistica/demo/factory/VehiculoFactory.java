package com.Logistica.demo.factory;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.Logistica.demo.dto.ResultadoValidacion;
import com.Logistica.demo.model.Vehiculo;
import com.Logistica.demo.repository.VehiculoRepository;
import com.Logistica.demo.strategy.ValidadorVehiculo;

/**
 * Factory concreta para crear Vehículos
 * Aplica validaciones y reglas de negocio específicas de vehículos
 * Implementa el Factory Pattern
 */
@Component
public class VehiculoFactory implements RecursoFactory<Vehiculo> {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private ValidadorVehiculo validador;

    @Override
    public Vehiculo crear(Vehiculo vehiculo) {
        // Aplicar validación
        ResultadoValidacion resultado = validador.validar(vehiculo);
        if (!resultado.isValido()) {
            throw new IllegalArgumentException(
                String.format("Validación fallida: %s (Código: %s)", 
                    resultado.getMensaje(), 
                    resultado.getCodigoError())
            );
        }

        // Aplicar reglas de negocio
        if (vehiculo.getEstado() == null) {
            vehiculo.setEstado("Disponible");
        }

        // Persistir
        return vehiculoRepository.save(vehiculo);
    }

    @Override
    public Vehiculo actualizar(Long id, Vehiculo detalles) {
        return vehiculoRepository.findById(id)
            .map(vehiculoExistente -> {
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
                vehiculoExistente.setPatente(detalles.getPatente());
                vehiculoExistente.setModelo(detalles.getModelo());
                vehiculoExistente.setChofer(detalles.getChofer());
                vehiculoExistente.setCapacidadCarga(detalles.getCapacidadCarga());
                vehiculoExistente.setEstado(detalles.getEstado());

                return vehiculoRepository.save(vehiculoExistente);
            })
            .orElseThrow(() -> new IllegalArgumentException("Vehículo con ID " + id + " no encontrado"));
    }

    @Override
    public Optional<Vehiculo> obtenerPorId(Long id) {
        return vehiculoRepository.findById(id);
    }

    @Override
    public Class<Vehiculo> getTipoEntidad() {
        return Vehiculo.class;
    }
}
