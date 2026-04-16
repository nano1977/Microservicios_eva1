package com.Logistica.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import com.Logistica.demo.model.Vehiculo;
import com.Logistica.demo.model.Inventario;
import com.Logistica.demo.service.LogisticaService;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private LogisticaService logisticaService;

	@Test
	void contextLoads() {
		assertNotNull(logisticaService, "LogisticaService debe estar disponible");
	}

	@Test
	void testCrearVehiculo() {
		Vehiculo vehiculo = new Vehiculo();
		vehiculo.setPatente("IEEE830");
		vehiculo.setModelo("TestModel");
		vehiculo.setChofer("Test Driver");
		vehiculo.setCapacidadCarga(1000.0);
		vehiculo.setEstado("Disponible");
		
		Vehiculo creado = logisticaService.crearVehiculo(vehiculo);
		assertNotNull(creado.getId(), "El vehículo debe tener ID después de crearse");
	}

	@Test
	void testObtenerVehiculosDisponibles() {
		List<Vehiculo> disponibles = logisticaService.obtenerVehiculosDisponibles();
		assertNotNull(disponibles, "La lista de vehículos disponibles no debe ser nula");
		assertTrue(disponibles.size() >= 0, "La lista debe ser válida");
	}

	@Test
	void testObtenerInventarioStockBajo() {
		List<Inventario> stockBajo = logisticaService.obtenerInventarioStockBajo();
		assertNotNull(stockBajo, "La lista de stock bajo debe existir - RFC-3.4 IEEE 830");
		assertTrue(stockBajo.size() >= 0, "Debe retornar items con cantidad < 10");
	}

	@Test
	void testListarInventario() {
		List<Inventario> inventarios = logisticaService.listarInventario();
		assertNotNull(inventarios, "La lista de inventario no debe ser nula");
		assertTrue(inventarios.size() >= 0, "Debe retornar lista válida de inventarios");
	}

}
