package com.Logistica.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Logistica.demo.audit.AuditoriaService;
import com.Logistica.demo.factory.CentroAcopioFactory;
import com.Logistica.demo.factory.InventarioFactory;
import com.Logistica.demo.factory.VehiculoFactory;
import com.Logistica.demo.model.CentroAcopio;
import com.Logistica.demo.model.Inventario;
import com.Logistica.demo.model.Vehiculo;
import com.Logistica.demo.repository.CentroAcopioRepository;
import com.Logistica.demo.repository.InventarioRepository;
import com.Logistica.demo.repository.VehiculoRepository;

/**
 * Capa de Servicio que encapsula la lógica de negocio de Logística
 * Implementa el patrón Service Layer
 * Orquesta el uso de Factories y Repositories
 * Centraliza las reglas de negocio de la aplicación
 */
@Service
@Transactional
public class LogisticaService {

    @Autowired
    private VehiculoFactory vehiculoFactory;

    @Autowired
    private CentroAcopioFactory centroAcopioFactory;

    @Autowired
    private InventarioFactory inventarioFactory;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private CentroAcopioRepository centroAcopioRepository;

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private AuditoriaService auditoriaService;

    // ==================== OPERACIONES VEHÍCULOS ====================

    /**
     * Crea un nuevo vehículo con validaciones
     * @param vehiculo el vehículo a crear
     * @return el vehículo creado
     */
    public Vehiculo crearVehiculo(Vehiculo vehiculo) {
        Vehiculo creado = vehiculoFactory.crear(vehiculo);
        registrarAuditoriaSafe("CREAR", "Vehiculo", creado.getId(), describirVehiculo(creado));
        return creado;
    }

    /**
     * Actualiza un vehículo existente
     * @param id el identificador del vehículo
     * @param detalles los nuevos datos
     * @return el vehículo actualizado
     */
    public Vehiculo actualizarVehiculo(Long id, Vehiculo detalles) {
        Vehiculo actualizado = vehiculoFactory.actualizar(id, detalles);
        registrarAuditoriaSafe("ACTUALIZAR", "Vehiculo", actualizado.getId(), describirVehiculo(actualizado));
        return actualizado;
    }

    /**
     * Obtiene un vehículo por su identificador
     * @param id el identificador
     * @return Optional con el vehículo
     */
    public Optional<Vehiculo> obtenerVehiculo(Long id) {
        return vehiculoFactory.obtenerPorId(id);
    }

    /**
     * Lista todos los vehículos
     * @return lista de vehículos
     */
    public List<Vehiculo> listarVehiculos() {
        return vehiculoRepository.findAll();
    }

    /**
     * Obtiene únicamente los vehículos disponibles (lógica de negocio)
     * @return lista de vehículos disponibles y con capacidad
     */
    public List<Vehiculo> obtenerVehiculosDisponibles() {
        return vehiculoRepository.findAll().stream()
            .filter(v -> "Disponible".equals(v.getEstado()))
            .filter(v -> v.getCapacidadCarga() > 0)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene vehículos en mantenimiento
     * @return lista de vehículos en mantenimiento
     */
    public List<Vehiculo> obtenerVehiculosEnMantenimiento() {
        return vehiculoRepository.findAll().stream()
            .filter(v -> "Mantenimiento".equals(v.getEstado()))
            .collect(Collectors.toList());
    }

    /**
     * Cambia el estado de un vehículo
     * Lógica de negocio: validar transiciones de estado
     * @param id el identificador del vehículo
     * @param nuevoEstado el nuevo estado
     * @return el vehículo con estado actualizado
     */
    public Vehiculo cambiarEstadoVehiculo(Long id, String nuevoEstado) {
        return vehiculoRepository.findById(id)
            .map(vehiculo -> {
                String estadoActual = vehiculo.getEstado();

                // Validar transición de estado
                if (!esTransicionValida(estadoActual, nuevoEstado)) {
                    throw new IllegalArgumentException(
                        String.format("Transición no permitida: %s -> %s", estadoActual, nuevoEstado)
                    );
                }

                vehiculo.setEstado(nuevoEstado);
                return vehiculoRepository.save(vehiculo);
            })
            .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado: " + id));
    }

    /**
     * Elimina un vehículo
     * @param id el identificador del vehículo
     */
    public void eliminarVehiculo(Long id) {
        vehiculoRepository.findById(id).ifPresent(vehiculo -> {
            vehiculoRepository.delete(vehiculo);
            registrarAuditoriaSafe("ELIMINAR", "Vehiculo", id, describirVehiculo(vehiculo));
        });
    }

    // ==================== OPERACIONES CENTROS DE ACOPIO ====================

    /**
     * Crea un nuevo centro de acopio con validaciones
     * @param centro el centro a crear
     * @return el centro creado
     */
    public CentroAcopio crearCentro(CentroAcopio centro) {
        CentroAcopio creado = centroAcopioFactory.crear(centro);
        registrarAuditoriaSafe("CREAR", "CentroAcopio", creado.getId(), describirCentro(creado));
        return creado;
    }

    /**
     * Actualiza un centro de acopio existente
     * @param id el identificador del centro
     * @param detalles los nuevos datos
     * @return el centro actualizado
     */
    public CentroAcopio actualizarCentro(Long id, CentroAcopio detalles) {
        CentroAcopio actualizado = centroAcopioFactory.actualizar(id, detalles);
        registrarAuditoriaSafe("ACTUALIZAR", "CentroAcopio", actualizado.getId(), describirCentro(actualizado));
        return actualizado;
    }

    /**
     * Obtiene un centro de acopio por su identificador
     * @param id el identificador
     * @return Optional con el centro
     */
    public Optional<CentroAcopio> obtenerCentro(Long id) {
        return centroAcopioFactory.obtenerPorId(id);
    }

    /**
     * Lista todos los centros de acopio
     * @return lista de centros
     */
    public List<CentroAcopio> listarCentros() {
        return centroAcopioRepository.findAll();
    }

    /**
     * Elimina un centro de acopio
     * @param id el identificador del centro
     */
    public void eliminarCentro(Long id) {
        centroAcopioRepository.findById(id).ifPresent(centro -> {
            centroAcopioRepository.delete(centro);
            registrarAuditoriaSafe("ELIMINAR", "CentroAcopio", id, describirCentro(centro));
        });
    }

    // ==================== OPERACIONES INVENTARIO ====================

    /**
     * Crea un nuevo item de inventario con validaciones
     * @param inventario el item a crear
     * @return el item creado
     */
    public Inventario crearInventario(Inventario inventario) {
        Inventario creado = inventarioFactory.crear(inventario);
        registrarAuditoriaSafe("CREAR", "Inventario", creado.getId(), describirInventario(creado));
        return creado;
    }

    /**
     * Actualiza un item de inventario existente
     * @param id el identificador del item
     * @param detalles los nuevos datos
     * @return el item actualizado
     */
    public Inventario actualizarInventario(Long id, Inventario detalles) {
        Inventario actualizado = inventarioFactory.actualizar(id, detalles);
        registrarAuditoriaSafe("ACTUALIZAR", "Inventario", actualizado.getId(), describirInventario(actualizado));
        return actualizado;
    }

    /**
     * Obtiene un item de inventario por su identificador
     * @param id el identificador
     * @return Optional con el item
     */
    public Optional<Inventario> obtenerInventario(Long id) {
        return inventarioFactory.obtenerPorId(id);
    }

    /**
     * Lista todos los items de inventario
     * @return lista de inventarios
     */
    public List<Inventario> listarInventario() {
        return inventarioRepository.findAll();
    }

    /**
     * Obtiene el inventario de un centro específico (lógica de negocio)
     * @param centroPorId el identificador del centro
     * @return lista de inventarios del centro
     */
    public List<Inventario> obtenerInventarioPorCentro(Long centroPorId) {
        return inventarioRepository.findAll().stream()
            .filter(inv -> inv.getCentroAcopio() != null 
                && inv.getCentroAcopio().getId().equals(centroPorId))
            .collect(Collectors.toList());
    }

    /**
     * Obtiene el total de un recurso en todos los centros (lógica de negocio)
     * @param tipoRecurso el tipo de recurso
     * @return total de cantidad disponible
     */
    public Integer obtenerTotalRecurso(String tipoRecurso) {
        return inventarioRepository.findAll().stream()
            .filter(inv -> inv.getRecurso().equals(tipoRecurso))
            .mapToInt(Inventario::getCantidad)
            .sum();
    }

    /**
     * Transfiere inventario entre centros (lógica de negocio compleja)
     * @param inventarioId el identificador del item de inventario
     * @param centroDestinoId el identificador del centro destino
     * @param cantidad la cantidad a transferir
     * @return el item transferido
     */
    public Inventario transferirInventario(Long inventarioId, Long centroDestinoId, Integer cantidad) {
        Inventario inventarioOrigen = inventarioRepository.findById(inventarioId)
            .orElseThrow(() -> new IllegalArgumentException("Inventario no encontrado: " + inventarioId));

        CentroAcopio centroDestino = centroAcopioRepository.findById(centroDestinoId)
            .orElseThrow(() -> new IllegalArgumentException("Centro destino no encontrado: " + centroDestinoId));

        // Validar cantidad disponible
        if (inventarioOrigen.getCantidad() < cantidad) {
            throw new IllegalArgumentException(
                String.format("Cantidad insuficiente. Disponible: %d, Solicitado: %d",
                    inventarioOrigen.getCantidad(), cantidad)
            );
        }

        // Reducir cantidad en origen
        inventarioOrigen.setCantidad(inventarioOrigen.getCantidad() - cantidad);
        inventarioRepository.save(inventarioOrigen);

        // Crear nuevo registro en destino
        Inventario inventarioDestino = new Inventario();
        inventarioDestino.setRecurso(inventarioOrigen.getRecurso());
        inventarioDestino.setCantidad(cantidad);
        inventarioDestino.setUnidadMedida(inventarioOrigen.getUnidadMedida());
        inventarioDestino.setCentroAcopio(centroDestino);

        return inventarioFactory.crear(inventarioDestino);
    }

    /**
     * Elimina un item de inventario
     * @param id el identificador del item
     */
    public void eliminarInventario(Long id) {
        inventarioRepository.findById(id).ifPresent(inventario -> {
            inventarioRepository.delete(inventario);
            registrarAuditoriaSafe("ELIMINAR", "Inventario", id, describirInventario(inventario));
        });
    }

    /**
     * Obtiene items de inventario con stock bajo (cantidad < 10)
     * Implementa RF-3.4 del IEEE 830: Generar alertas de stock bajo
     * @return lista de inventarios con cantidad < 10
     */
    public List<Inventario> obtenerInventarioStockBajo() {
        return inventarioRepository.findAll().stream()
            .filter(inv -> inv.getCantidad() < 10)
            .collect(Collectors.toList());
    }

    /**
     * Verifica si existe stock bajo en un centro específico
     * @param centroPorId el identificador del centro
     * @return true si hay items con stock bajo
     */
    public boolean existeStockBajoEnCentro(Long centroPorId) {
        return inventarioRepository.findAll().stream()
            .anyMatch(inv -> inv.getCentroAcopio() != null 
                && inv.getCentroAcopio().getId().equals(centroPorId)
                && inv.getCantidad() < 10);
    }

    // ==================== MÉTODOS AUXILIARES ===================="

    /**
     * Valida si una transición de estado es válida
     * Lógica de negocio para transiciones permitidas
     */
    private boolean esTransicionValida(String estadoActual, String nuevoEstado) {
        if (estadoActual.equals(nuevoEstado)) {
            return true; // Mismo estado es válido
        }

        // Definir transiciones válidas
        // Disponible -> En Ruta, Mantenimiento
        // En Ruta -> Disponible, Mantenimiento
        // Mantenimiento -> Disponible
        return true; // Por simplicidad, todas las transiciones son válidas ahora
    }

    private void registrarAuditoriaSafe(String accion, String tipoRecurso, Long idRecurso, String detalles) {
        try {
            auditoriaService.registrarAccion(accion, tipoRecurso, idRecurso, "Sistema", "Administrador", detalles);
        } catch (RuntimeException ex) {
            // Evita bloquear el flujo si la auditoria falla.
        }
    }

    private String describirVehiculo(Vehiculo vehiculo) {
        return String.format("patente=%s, modelo=%s, chofer=%s, capacidad=%s, estado=%s",
            vehiculo.getPatente(), vehiculo.getModelo(), vehiculo.getChofer(),
            vehiculo.getCapacidadCarga(), vehiculo.getEstado());
    }

    private String describirCentro(CentroAcopio centro) {
        return String.format("nombre=%s, ubicacion=%s, contacto=%s, capacidadMaxima=%s",
            centro.getNombre(), centro.getUbicacion(), centro.getContacto(), centro.getCapacidadMaxima());
    }

    private String describirInventario(Inventario inventario) {
        String centroId = inventario.getCentroAcopio() != null ? String.valueOf(inventario.getCentroAcopio().getId()) : "";
        String centroNombre = inventario.getCentroAcopio() != null ? inventario.getCentroAcopio().getNombre() : "";
        return String.format("recurso=%s, cantidad=%s, unidad=%s, centroId=%s, centroNombre=%s",
            inventario.getRecurso(), inventario.getCantidad(), inventario.getUnidadMedida(), centroId, centroNombre);
    }
}
