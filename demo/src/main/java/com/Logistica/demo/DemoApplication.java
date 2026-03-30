package com.Logistica.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.Logistica.demo.model.Vehiculo;
import com.Logistica.demo.repository.VehiculoRepository;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(VehiculoRepository repository) {
		return args -> {
			Vehiculo v = new Vehiculo();
			v.setPatente("AB-CD-12");
			v.setModelo("Volvo FH16 - Camión de Carga");
			v.setChofer("Juan Pérez");
			
			repository.save(v);
			
			System.out.println(">>> Vehículo de prueba guardado exitosamente");
		};
	}
}
