package com.Logistica.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.Logistica.demo.model.CentroAcopio;
import com.Logistica.demo.model.Inventario;
import com.Logistica.demo.model.Vehiculo;
import com.Logistica.demo.repository.CentroAcopioRepository;
import com.Logistica.demo.repository.InventarioRepository;
import com.Logistica.demo.repository.VehiculoRepository;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initDatabase(VehiculoRepository vehiculoRepo, CentroAcopioRepository centroRepo, InventarioRepository inventarioRepo) {
		return args -> {
			// ===== CREAR VEHÍCULOS DE PRUEBA =====
			Vehiculo v1 = new Vehiculo();
			v1.setPatente("HELP-01");
			v1.setModelo("Camión Frigorífico");
			v1.setChofer("Juan Pérez");
			v1.setCapacidadCarga(5000.0);
			v1.setEstado("Disponible");
			vehiculoRepo.save(v1);

			CentroAcopio c1 = new CentroAcopio();
			c1.setNombre("Centro Regional Sur");
			c1.setUbicacion("Concepción, Chile");
			c1.setContacto("+569 1234 5678");
			centroRepo.save(c1);

			Inventario i1 = new Inventario();
			i1.setRecurso("Kits de Alimentos");
			i1.setCantidad(200);
			i1.setCentroAcopio(c1);
			inventarioRepo.save(i1);

			System.out.println(">>> Sistema Donaton: Datos inicializados correctamente.");
		};
	}
}