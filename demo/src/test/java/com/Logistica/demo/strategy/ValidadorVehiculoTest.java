package com.Logistica.demo.strategy;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import com.Logistica.demo.dto.ResultadoValidacion;
import com.Logistica.demo.model.Vehiculo;

/**
 * Tests para validar el Strategy Pattern - ValidadorVehiculo
 * Comprueba que las validaciones se aplican correctamente
 */
class ValidadorVehiculoTest {

    private ValidadorVehiculo validador;
    private Vehiculo vehiculoValido;

    @BeforeEach
    void setUp() {
        validador = new ValidadorVehiculo();
        
        // Crear un vehículo válido de ejemplo
        vehiculoValido = new Vehiculo();
        vehiculoValido.setPatente("HELP-01");
        vehiculoValido.setModelo("Camión Frigorífico");
        vehiculoValido.setChofer("Juan Pérez");
        vehiculoValido.setCapacidadCarga(5000.0);
        vehiculoValido.setEstado("Disponible");
    }

    @Test
    void testVehiculoValidoSonValidaciones() {
        // GIVEN: Un vehículo válido
        // WHEN: Se valida
        ResultadoValidacion resultado = validador.validar(vehiculoValido);
        
        // THEN: Debe ser válido
        assertTrue(resultado.isValido(), "El vehículo válido debería pasar validación");
        assertEquals("OK", resultado.getCodigoError());
    }

    @Test
    void testPatenteObligatoria() {
        // GIVEN: Vehículo sin patente
        vehiculoValido.setPatente(null);
        
        // WHEN: Se valida
        ResultadoValidacion resultado = validador.validar(vehiculoValido);
        
        // THEN: Debe fallar
        assertFalse(resultado.isValido(), "Vehículo sin patente debería fallar");
        assertEquals("PATENTE_REQUIRED", resultado.getCodigoError());
    }

    @Test
    void testPatenteMinimaDeLongitud() {
        // GIVEN: Patente muy corta
        vehiculoValido.setPatente("AB");
        
        // WHEN: Se valida
        ResultadoValidacion resultado = validador.validar(vehiculoValido);
        
        // THEN: Debe fallar
        assertFalse(resultado.isValido());
        assertEquals("PATENTE_LENGTH", resultado.getCodigoError());
    }

    @Test
    void testCapacidadMayor0() {
        // GIVEN: Capacidad negativa
        vehiculoValido.setCapacidadCarga(-100.0);
        
        // WHEN: Se valida
        ResultadoValidacion resultado = validador.validar(vehiculoValido);
        
        // THEN: Debe fallar
        assertFalse(resultado.isValido());
        assertEquals("CAPACIDAD_INVALID", resultado.getCodigoError());
    }

    @Test
    void testCapacidadMaximaPermitida() {
        // GIVEN: Capacidad excesiva
        vehiculoValido.setCapacidadCarga(200000.0); // Mayor que máximo
        
        // WHEN: Se valida
        ResultadoValidacion resultado = validador.validar(vehiculoValido);
        
        // THEN: Debe fallar
        assertFalse(resultado.isValido());
        assertEquals("CAPACIDAD_MAX", resultado.getCodigoError());
    }

    @Test
    void testEstadoValido() {
        // GIVEN: Estado válido
        vehiculoValido.setEstado("En Ruta");
        
        // WHEN: Se valida
        ResultadoValidacion resultado = validador.validar(vehiculoValido);
        
        // THEN: Debe pasar
        assertTrue(resultado.isValido());
    }

    @Test
    void testEstadoInvalido() {
        // GIVEN: Estado inválido
        vehiculoValido.setEstado("Desaparecido");
        
        // WHEN: Se valida
        ResultadoValidacion resultado = validador.validar(vehiculoValido);
        
        // THEN: Debe fallar
        assertFalse(resultado.isValido());
        assertEquals("ESTADO_INVALID", resultado.getCodigoError());
    }

    @Test
    void testTipoDeEntidad() {
        // WHEN: Se consulta el tipo de entidad
        // THEN: Debe retornar Vehiculo.class
        assertEquals(Vehiculo.class, validador.getTipoEntidad());
    }
}
