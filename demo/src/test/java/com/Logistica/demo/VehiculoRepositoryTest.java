package com.Logistica.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.Logistica.demo.model.Vehiculo;
import com.Logistica.demo.repository.VehiculoRepository;

@SpringBootTest
public class VehiculoRepositoryTest {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Test
    public void testCrearYGuardarVehiculo() {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPatente("TEST-001");
        vehiculo.setModelo("Camión Prueba");
        vehiculo.setChofer("Juan");
        vehiculo.setCapacidadCarga(5000.0);
        vehiculo.setEstado("Disponible");

        Vehiculo guardado = vehiculoRepository.save(vehiculo);

        assertNotNull(guardado.getId());
        assertEquals("TEST-001", guardado.getPatente());
        assertEquals("Disponible", guardado.getEstado());
    }

    @Test
    public void testListarVehiculos() {
        Vehiculo v1 = new Vehiculo();
        v1.setPatente("V001");
        v1.setModelo("Camión");
        v1.setChofer("Driver1");
        vehiculoRepository.save(v1);

        Vehiculo v2 = new Vehiculo();
        v2.setPatente("V002");
        v2.setModelo("Furgón");
        v2.setChofer("Driver2");
        vehiculoRepository.save(v2);

        long count = vehiculoRepository.count();
        assertNotNull(count);
    }

    @Test
    public void testActualizarVehiculo() {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPatente("UPD-001");
        vehiculo.setEstado("Disponible");
        Vehiculo guardado = vehiculoRepository.save(vehiculo);

        guardado.setEstado("En Ruta");
        Vehiculo actualizado = vehiculoRepository.save(guardado);

        assertNotNull(actualizado);
        assertEquals("En Ruta", actualizado.getEstado());
    }

    @Test
    public void testEliminarVehiculo() {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPatente("DEL-001");
        Vehiculo guardado = vehiculoRepository.save(vehiculo);

        vehiculoRepository.deleteById(guardado.getId());
        assertTrue(vehiculoRepository.findById(guardado.getId()).isEmpty());
    }
}
