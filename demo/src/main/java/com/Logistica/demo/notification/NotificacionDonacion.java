package com.Logistica.demo.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DTO para notificación de donación recibida
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionDonacion {
    
    private String numeroTicket;
    private String nombreDonante;
    private String emailDonante;
    private String nombreReceptor;
    private String emailReceptor;
    private String centroDonacion;
    private String tipoRecurso;
    private Integer cantidad;
    private String unidad;
    private LocalDateTime fecha;
    private String mensaje;

    /**
     * Genera el formato de notificación para email
     */
    public String obtenerMensajeEmail() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        return String.format(
            "==============================================\n" +
            "        COMPROBANTE DE DONACIÓN RECIBIDA\n" +
            "==============================================\n\n" +
            "Número de Ticket: %s\n\n" +
            "INFORMACIÓN DEL DONANTE:\n" +
            "  Nombre: %s\n" +
            "  Email: %s\n\n" +
            "CENTRO QUE RECIBE:\n" +
            "  Centro: %s\n" +
            "  Receptor: %s\n" +
            "  Email: %s\n\n" +
            "DONACIÓN RECIBIDA:\n" +
            "  Tipo: %s\n" +
            "  Cantidad: %d %s\n" +
            "  Fecha y Hora: %s\n\n" +
            "==============================================\n" +
            "¡Gracias por tu generosidad! 🙏\n" +
            "Tu donación llegará a quienes más la necesitan.\n" +
            "==============================================",
            numeroTicket,
            nombreDonante, emailDonante,
            centroDonacion, nombreReceptor, emailReceptor,
            tipoRecurso, cantidad, unidad,
            fecha.format(formato)
        );
    }

    /**
     * Genera el formato de notificación para SMS (versión corta)
     */
    public String obtenerMensajeSMS() {
        return String.format(
            "Donación recibida! Ticket: %s. %d %s de %s en %s. Gracias!",
            numeroTicket, cantidad, unidad, tipoRecurso, centroDonacion
        );
    }

    /**
     * Genera el formato de notificación para el receptor
     */
    public String obtenerMensajeReceptor() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        return String.format(
            "Hola %s,\n\n" +
            "Has recibido una donación:\n" +
            "• Donante: %s\n" +
            "• Tipo: %s\n" +
            "• Cantidad: %d %s\n" +
            "• Fecha: %s\n" +
            "• Ticket: %s\n\n" +
            "Actualiza tu sistema para procesar la donación.\n\n" +
            "Sistema Donaton",
            nombreReceptor, nombreDonante, tipoRecurso, 
            cantidad, unidad, fecha.format(formato), numeroTicket
        );
    }
}
