package com.Logistica.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Logistica.demo.model.CentroAcopio;
import com.Logistica.demo.model.Inventario;
import com.Logistica.demo.model.Vehiculo;
import com.Logistica.demo.repository.CentroAcopioRepository;
import com.Logistica.demo.repository.InventarioRepository;
import com.Logistica.demo.repository.VehiculoRepository;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/logistica")
@Tag(name = "Logística", description = "API para gestión de vehículos, centros de acopio e inventario")
public class LogisticaController {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private CentroAcopioRepository centroAcopioRepository;

@Autowired
private InventarioRepository inventarioRepository;

    // LEER TODOS (Read)
    @GetMapping("/vehiculos")
    public List<Vehiculo> listar() {
        return vehiculoRepository.findAll();
    }

    // CREAR (Create) 
    @PostMapping("/vehiculos")
    public Vehiculo crear(@RequestBody Vehiculo vehiculo) {
        if (vehiculo.getEstado() == null) vehiculo.setEstado("Disponible");
        return vehiculoRepository.save(vehiculo);
    }

    // ACTUALIZAR (Update) - Útil para cambiar el chofer o el estado 
    @PutMapping("/vehiculos/{id}")
    public Vehiculo actualizar(@PathVariable Long id, @RequestBody Vehiculo detalles) {
        return vehiculoRepository.findById(id)
            .map(v -> {
                v.setChofer(detalles.getChofer());
                v.setEstado(detalles.getEstado());
                v.setCapacidadCarga(detalles.getCapacidadCarga());
                return vehiculoRepository.save(v);
            }).orElseThrow();
    }
    
    @GetMapping("/centros")
    public List<CentroAcopio> listarCentros() {
        return centroAcopioRepository.findAll();
    }

    @GetMapping("/inventario")
    public List<Inventario> listarInventario() {
        return inventarioRepository.findAll();
    }
    
    // --- MÉTODOS PARA CENTROS DE ACOPIO ---
    @PostMapping("/centros")
    public CentroAcopio crearCentro(@RequestBody CentroAcopio centro) {
        return centroAcopioRepository.save(centro);
    }

    // --- MÉTODOS PARA INVENTARIO ---
    @PostMapping("/inventario")
    public Inventario crearInventario(@RequestBody Inventario item) {
        return inventarioRepository.save(item);
    }

    // ELIMINAR (Delete)
    @DeleteMapping("/vehiculos/{id}")
    public void eliminar(@PathVariable Long id) {
        vehiculoRepository.deleteById(id);
    }
}