package com.Logistica.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Logistica.demo.model.Vehiculo;
import com.Logistica.demo.repository.VehiculoRepository;

@RestController
@RequestMapping("/api/logistica") // Esto organiza mejor tus rutas
public class LogisticaController {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @GetMapping("/saludo")
    public String holaLogistica() {
        return "¡Hola! El microservicio de Logística está funcionando correctamente.";
    }

    // Nueva ruta para ver los vehículos
    @GetMapping("/vehiculos")
    public List<Vehiculo> listarVehiculos() {
        return vehiculoRepository.findAll();
    }
}