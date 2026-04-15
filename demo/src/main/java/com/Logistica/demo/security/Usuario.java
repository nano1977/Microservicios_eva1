package com.Logistica.demo.security;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidad que representa un Usuario del Sistema
 * Incluye autenticación con 2FA (Dos Factores)
 */
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String contraseña; // En producción: HASH SHA-256 o bcrypt
    
    @Column(nullable = false)
    private String rol; // DONANTE, RECEPTOR, ADMIN
    
    // 2FA - Autenticación de Dos Factores
    @Column(nullable = false)
    private Boolean dos2FAActivado = true; // Por defecto activado
    
    @Column(unique = true)
    private String telefonoParaSMS; // Para enviar código 2FA por SMS
    
    @Column
    private String codigoTemporal; // Código 2FA generado (6 dígitos)
    
    @Column
    private LocalDateTime fechaGeneracionCodigoTemporal;
    
    @Column
    private Boolean codigoTemporalValidado = false;
    
    // Auditoría
    @Column(nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();
    
    @Column
    private LocalDateTime ultimoLogin;
    
    @Column
    private Integer intentosFallidosLogin = 0;
    
    @Column
    private Boolean activo = true;
    
    @Column(columnDefinition = "TEXT")
    private String notas; // Información adicional
    
    @Override
    public String toString() {
        return String.format("Usuario[%s - %s - %s]", email, nombre, rol);
    }
}
