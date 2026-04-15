package com.Logistica.demo.strategy;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import com.Logistica.demo.dto.ResultadoValidacion;
import com.Logistica.demo.model.CentroAcopio;

/**
 * Tests para validar el Strategy Pattern - ValidadorCentroAcopio
 * Comprueba que las validaciones se aplican correctamente
 */
class ValidadorCentroAcopioTest {

    private ValidadorCentroAcopio validador;
    private CentroAcopio centroValido;

    @BeforeEach
    void setUp() {
        validador = new ValidadorCentroAcopio();
        
        // Crear un centro de acopio válido
        centroValido = new CentroAcopio();
        centroValido.setNombre("Centro Regional Sur");
        centroValido.setUbicacion("Región Metropolitana");
        centroValido.setContacto("+56912345678");
        centroValido.setCapacidadMaxima("10000");
    }

    @Test
    void testCentroValido() {
        // GIVEN: Centro válido
        // WHEN: Se valida
        ResultadoValidacion resultado = validador.validar(centroValido);
        
        // THEN: Debe ser válido
        assertTrue(resultado.isValido());
        assertEquals("OK", resultado.getCodigoError());
    }

    @Test
    void testNombreObligatorio() {
        // GIVEN: Centro sin nombre
        centroValido.setNombre(null);
        
        // WHEN: Se valida
        ResultadoValidacion resultado = validador.validar(centroValido);
        
        // THEN: Debe fallar
        assertFalse(resultado.isValido());
        assertEquals("NOMBRE_REQUIRED", resultado.getCodigoError());
    }

    @Test
    void testNombreLongitudMinima() {
        // GIVEN: Nombre muy corto
        centroValido.setNombre("AB");
        
        // WHEN: Se valida
        ResultadoValidacion resultado = validador.validar(centroValido);
        
        // THEN: Debe fallar
        assertFalse(resultado.isValido());
        assertEquals("NOMBRE_LENGTH", resultado.getCodigoError());
    }

    @Test
    void testContactoFormatoValido() {
        // GIVEN: Contacto válido
        centroValido.setContacto("56912345678");
        
        // WHEN: Se valida
        ResultadoValidacion resultado = validador.validar(centroValido);
        
        // THEN: Debe pasar
        assertTrue(resultado.isValido());
    }

    @Test
    void testContactoFormatoInvalido() {
        // GIVEN: Contacto con caracteres inválidos
        centroValido.setContacto("abc123def45");
        
        // WHEN: Se valida
        ResultadoValidacion resultado = validador.validar(centroValido);
        
        // THEN: Debe fallar
        assertFalse(resultado.isValido());
        assertEquals("CONTACTO_FORMAT", resultado.getCodigoError());
    }

    @Test
    void testCapacidadNumericaValida() {
        // GIVEN: Capacidad válida
        centroValido.setCapacidadMaxima("5000.5");
        
        // WHEN: Se valida
        ResultadoValidacion resultado = validador.validar(centroValido);
        
        // THEN: Debe pasar
        assertTrue(resultado.isValido());
    }

    @Test
    void testCapacidadNoNumerica() {
        // GIVEN: Capacidad con texto
        centroValido.setCapacidadMaxima("mucha");
        
        // WHEN: Se valida
        ResultadoValidacion resultado = validador.validar(centroValido);
        
        // THEN: Debe fallar
        assertFalse(resultado.isValido());
        assertEquals("CAPACIDAD_FORMAT", resultado.getCodigoError());
    }

    @Test
    void testTipoDeEntidad() {
        // WHEN: Se consulta el tipo de entidad
        // THEN: Debe retornar CentroAcopio.class
        assertEquals(CentroAcopio.class, validador.getTipoEntidad());
    }
}
