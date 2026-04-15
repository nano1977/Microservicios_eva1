package com.Logistica.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.Logistica.demo.service.LogisticaService;
import com.Logistica.demo.dto.ResultadoValidacion;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

/**
 * Controlador REST para gestión de logística
 * Utiliza LogisticaService (Service Layer)
 * Delega toda la lógica de negocio al servicio
 * Implementa los patrones Strategy y Factory mediante el servicio
 */
@RestController
@RequestMapping("/api/logistica")
@Tag(name = "Logística", description = "API para gestión de vehículos, centros de acopio e inventario")
public class LogisticaController {

    @Autowired
    private LogisticaService logisticaService;

    // ==================== VEHÍCULOS ====================

    @GetMapping("/vehiculos")
    @Operation(summary = "Lista todos los vehículos")
    public ResponseEntity<List<Vehiculo>> listarVehiculos() {
        return ResponseEntity.ok(logisticaService.listarVehiculos());
    }

    @GetMapping("/vehiculos/disponibles")
    @Operation(summary = "Obtiene vehículos disponibles")
    public ResponseEntity<List<Vehiculo>> listarVehiculosDisponibles() {
        return ResponseEntity.ok(logisticaService.obtenerVehiculosDisponibles());
    }

    @GetMapping("/vehiculos/mantenimiento")
    @Operation(summary = "Obtiene vehículos en mantenimiento")
    public ResponseEntity<List<Vehiculo>> listarVehiculosMantenimiento() {
        return ResponseEntity.ok(logisticaService.obtenerVehiculosEnMantenimiento());
    }

    @GetMapping("/vehiculos/{id}")
    @Operation(summary = "Obtiene un vehículo por ID")
    public ResponseEntity<Vehiculo> obtenerVehiculo(@PathVariable Long id) {
        return logisticaService.obtenerVehiculo(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/vehiculos")
    @Operation(summary = "Crea un nuevo vehículo con validaciones")
    public ResponseEntity<?> crearVehiculo(@RequestBody Vehiculo vehiculo) {
        try {
            Vehiculo creado = logisticaService.crearVehiculo(vehiculo);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                new ResultadoValidacion(false, e.getMessage(), "VALIDATION_ERROR")
            );
        }
    }

    @PutMapping("/vehiculos/{id}")
    @Operation(summary = "Actualiza un vehículo existente")
    public ResponseEntity<?> actualizarVehiculo(@PathVariable Long id, @RequestBody Vehiculo detalles) {
        try {
            Vehiculo actualizado = logisticaService.actualizarVehiculo(id, detalles);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(
                new ResultadoValidacion(false, e.getMessage(), "VALIDATION_ERROR")
            );
        }
    }

    @PutMapping("/vehiculos/{id}/estado/{nuevoEstado}")
    @Operation(summary = "Cambia el estado de un vehículo")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @PathVariable String nuevoEstado) {
        try {
            Vehiculo actualizado = logisticaService.cambiarEstadoVehiculo(id, nuevoEstado);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(
                new ResultadoValidacion(false, e.getMessage(), "ESTADO_ERROR")
            );
        }
    }

    @DeleteMapping("/vehiculos/{id}")
    @Operation(summary = "Elimina un vehículo")
    public ResponseEntity<Void> eliminarVehiculo(@PathVariable Long id) {
        logisticaService.eliminarVehiculo(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== CENTROS DE ACOPIO ====================

    @GetMapping("/centros")
    @Operation(summary = "Lista todos los centros de acopio")
    public ResponseEntity<List<CentroAcopio>> listarCentros() {
        return ResponseEntity.ok(logisticaService.listarCentros());
    }

    @GetMapping("/centros/{id}")
    @Operation(summary = "Obtiene un centro de acopio por ID")
    public ResponseEntity<CentroAcopio> obtenerCentro(@PathVariable Long id) {
        return logisticaService.obtenerCentro(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/centros")
    @Operation(summary = "Crea un nuevo centro de acopio con validaciones")
    public ResponseEntity<?> crearCentro(@RequestBody CentroAcopio centro) {
        try {
            CentroAcopio creado = logisticaService.crearCentro(centro);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                new ResultadoValidacion(false, e.getMessage(), "VALIDATION_ERROR")
            );
        }
    }

    @PutMapping("/centros/{id}")
    @Operation(summary = "Actualiza un centro de acopio existente")
    public ResponseEntity<?> actualizarCentro(@PathVariable Long id, @RequestBody CentroAcopio detalles) {
        try {
            CentroAcopio actualizado = logisticaService.actualizarCentro(id, detalles);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(
                new ResultadoValidacion(false, e.getMessage(), "VALIDATION_ERROR")
            );
        }
    }

    @DeleteMapping("/centros/{id}")
    @Operation(summary = "Elimina un centro de acopio")
    public ResponseEntity<Void> eliminarCentro(@PathVariable Long id) {
        logisticaService.eliminarCentro(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== INVENTARIO ====================

    @GetMapping("/inventario")
    @Operation(summary = "Lista todos los items de inventario")
    public ResponseEntity<List<Inventario>> listarInventario() {
        return ResponseEntity.ok(logisticaService.listarInventario());
    }

    @GetMapping("/inventario/{id}")
    @Operation(summary = "Obtiene un item de inventario por ID")
    public ResponseEntity<Inventario> obtenerInventario(@PathVariable Long id) {
        return logisticaService.obtenerInventario(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/centros/{centroId}/inventario")
    @Operation(summary = "Obtiene el inventario de un centro específico")
    public ResponseEntity<List<Inventario>> obtenerInventarioPorCentro(@PathVariable Long centroId) {
        return ResponseEntity.ok(logisticaService.obtenerInventarioPorCentro(centroId));
    }

    @PostMapping("/inventario")
    @Operation(summary = "Crea un nuevo item de inventario con validaciones")
    public ResponseEntity<?> crearInventario(@RequestBody Inventario inventario) {
        try {
            Inventario creado = logisticaService.crearInventario(inventario);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                new ResultadoValidacion(false, e.getMessage(), "VALIDATION_ERROR")
            );
        }
    }

    @PutMapping("/inventario/{id}")
    @Operation(summary = "Actualiza un item de inventario")
    public ResponseEntity<?> actualizarInventario(@PathVariable Long id, @RequestBody Inventario detalles) {
        try {
            Inventario actualizado = logisticaService.actualizarInventario(id, detalles);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(
                new ResultadoValidacion(false, e.getMessage(), "VALIDATION_ERROR")
            );
        }
    }

    @PostMapping("/inventario/{id}/transferir")
    @Operation(summary = "Transfiere inventario a otro centro")
    public ResponseEntity<?> transferirInventario(
            @PathVariable Long id,
            @RequestBody TransferenciaDTO transferencia) {
        try {
            Inventario transferido = logisticaService.transferirInventario(
                id,
                transferencia.getCentroDestinoId(),
                transferencia.getCantidad()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(transferido);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(
                new ResultadoValidacion(false, e.getMessage(), "TRANSFER_ERROR")
            );
        }
    }

    @DeleteMapping("/inventario/{id}")
    @Operation(summary = "Elimina un item de inventario")
    public ResponseEntity<Void> eliminarInventario(@PathVariable Long id) {
        logisticaService.eliminarInventario(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== DTO AUXILIAR ====================

    /**
     * DTO para solicitudes de transferencia de inventario
     */
    public static class TransferenciaDTO {
        private Long centroDestinoId;
        private Integer cantidad;

        public Long getCentroDestinoId() {
            return centroDestinoId;
        }

        public void setCentroDestinoId(Long centroDestinoId) {
            this.centroDestinoId = centroDestinoId;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }
    }
}