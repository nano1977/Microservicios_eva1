package com.Logistica.demo.factory;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.Logistica.demo.model.Vehiculo;
import com.Logistica.demo.repository.VehiculoRepository;

/**
 * Tests de integración para el Factory Pattern - VehiculoFactory
 * Comprueba que la creación de vehículos funciona correctamente
 * con validaciones y reglas de negocio aplicadas
 */
@SpringBootTest
class VehiculoFactoryTest {

    @Autowired
    private VehiculoFactory vehiculoFactory;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    private Vehiculo vehiculoValido;

    @BeforeEach
    void setUp() {
        // Limpiar BD de prueba
        vehiculoRepository.deleteAll();
        
        // Crear vehículo válido
        vehiculoValido = new Vehiculo();
        vehiculoValido.setPatente("TEST-001");
        vehiculoValido.setModelo("Camión");
        vehiculoValido.setChofer("Test Driver");
        vehiculoValido.setCapacidadCarga(5000.0);
        vehiculoValido.setEstado("Disponible");
    }

    @Test
    void testCrearVehiculoValido() {
        // GIVEN: Vehículo válido
        // WHEN: Factory lo crea
        Vehiculo creado = vehiculoFactory.crear(vehiculoValido);
        
        // THEN: Debe persistirse con ID
        assertNotNull(creado.getId());
        assertEquals("TEST-001", creado.getPatente());
        assertEquals("Disponible", creado.getEstado());
    }

    @Test
    void testCrearVehiculoAsignaEstadoDefault() {
        // GIVEN: Vehículo sin estado
        vehiculoValido.setEstado(null);
        
        // WHEN: Factory lo crea (debe aplicar regla de negocio)
        Vehiculo creado = vehiculoFactory.crear(vehiculoValido);
        
        // THEN: Debe tener estado default "Disponible"
        assertEquals("Disponible", creado.getEstado());
    }

    @Test
    void testCrearVehiculoInvalidoLanzaExcepcion() {
        // GIVEN: Vehículo sin patente (inválido)
        vehiculoValido.setPatente(null);
        
        // WHEN/THEN: Factory debe lanzar excepción
        assertThrows(IllegalArgumentException.class, () -> {
            vehiculoFactory.crear(vehiculoValido);
        }, "Factory debería rechazar vehículo sin patente");
    }

    @Test
    void testActualizarVehiculo() {
        // GIVEN: Vehículo creado
        Vehiculo creado = vehiculoFactory.crear(vehiculoValido);
        Long id = creado.getId();
        
        // Preparar actualización
        Vehiculo detalles = new Vehiculo();
        detalles.setPatente("TEST-002");
        detalles.setModelo("Camión Actualizado");
        detalles.setChofer("New Driver");
        detalles.setCapacidadCarga(6000.0);
        detalles.setEstado("En Ruta");
        
        // WHEN: Se actualiza
        Vehiculo actualizado = vehiculoFactory.actualizar(id, detalles);
        
        // THEN: Debe tener nuevos valores
        assertEquals("New Driver", actualizado.getChofer());
        assertEquals("En Ruta", actualizado.getEstado());
    }

    @Test
    void testObtenerVehiculoPorId() {
        // GIVEN: Vehículo creado
        Vehiculo creado = vehiculoFactory.crear(vehiculoValido);
        Long id = creado.getId();
        
        // WHEN: Se obtiene por ID
        var resultado = vehiculoFactory.obtenerPorId(id);
        
        // THEN: Debe encontrarse
        assertTrue(resultado.isPresent());
        assertEquals("TEST-001", resultado.get().getPatente());
    }

    @Test
    void testMultiplesVehiculosConFactory() {
        // GIVEN: Múltiples vehículos válidos
        Vehiculo v1 = new Vehiculo();
        v1.setPatente("V001");
        v1.setModelo("Tipo 1");
        v1.setChofer("Chofer 1");
        v1.setCapacidadCarga(1000.0);
        
        Vehiculo v2 = new Vehiculo();
        v2.setPatente("V002");
        v2.setModelo("Tipo 2");
        v2.setChofer("Chofer 2");
        v2.setCapacidadCarga(2000.0);
        
        // WHEN: Se crean con Factory
        Vehiculo creado1 = vehiculoFactory.crear(v1);
        Vehiculo creado2 = vehiculoFactory.crear(v2);
        
        // THEN: Ambos deben tener IDs diferentes
        assertNotNull(creado1.getId());
        assertNotNull(creado2.getId());
        assertNotEquals(creado1.getId(), creado2.getId());
    }
}
