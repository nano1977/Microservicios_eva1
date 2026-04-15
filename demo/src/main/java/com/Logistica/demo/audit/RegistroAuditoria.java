package com.Logistica.demo.audit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidad que registra auditoría de toda acción en el sistema
 * Almacena: QUÉ se hizo, QUIÉN lo hizo, CUÁNDO, DÓNDE y DETALLES
 */
@Entity
@Table(name = "auditoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroAuditoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Qué se hizo
    @Column(nullable = false)
    private String accion; // CREAR, ACTUALIZAR, ELIMINAR, TRANSFERIR
    
    // Sobre qué
    @Column(nullable = false)
    private String tipoRecurso; // Vehiculo, CentroAcopio, Inventario, Donacion
    
    @Column(nullable = false)
    private Long idRecurso;
    
    // Quién lo hizo
    @Column(nullable = false)
    private String usuario; // Email o usuario que realizó la acción
    
    @Column(nullable = false)
    private String rol; // Donante, Receptor, Admin
    
    // Cuándo
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    // Detalles
    @Column(columnDefinition = "TEXT")
    private String detalles; // JSON con el cambio
    
    @Column(columnDefinition = "TEXT")
    private String cambiosAntes; // Valor anterior
    
    @Column(columnDefinition = "TEXT")
    private String cambiosDespues; // Valor nuevo
    
    // Información de la donación
    @Column
    private String centroDonacion; // Centro que recibe
    
    @Column
    private String nombreDonante; // Quién donó
    
    @Column
    private String emailDonante; // Email del donante
    
    @Column
    private String nombreReceptor; // Quién recibió
    
    @Column
    private String emailReceptor; // Email del receptor
    
    // Ticket para referencia
    @Column(unique = true)
    private String numeroTicket; // Ej: DON-20260415-00001
    
    // Status de la notificación
    @Column
    private Boolean notificacionEnviada = false;
    
    @Column
    private LocalDateTime fechaNotificacion;
    
    @Override
    public String toString() {
        return String.format(
            "Auditoría[%s-%d] %s por %s en %s",
            tipoRecurso, idRecurso, accion, usuario, timestamp
        );
    }
}
