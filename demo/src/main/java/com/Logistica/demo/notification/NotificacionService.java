package com.Logistica.demo.notification;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servicio que gestiona notificaciones de donaciones
 * Soporta: Email, SMS y Logs
 * En producción, integrar con servicios reales como SendGrid, Twilio, etc
 */
@Service
public class NotificacionService {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionService.class);

    /**
     * Envía notificación de donación recibida
     * En desarrollo: envía a logs
     * En producción: integrar con SendGrid/Twilio
     */
    public boolean enviarNotificacionDonacion(NotificacionDonacion notificacion) {
        try {
            logger.info("📧 NOTIFICACIÓN EMAIL ENVIADA");
            logger.info("================================");
            logger.info("Para: {} ({})", notificacion.getNombreDonante(), notificacion.getEmailDonante());
            logger.info("Asunto: Comprobante de Donación - Ticket {}", notificacion.getNumeroTicket());
            logger.info("================================");
            logger.info(notificacion.obtenerMensajeEmail());
            logger.info("================================\n");

            // En producción, aquí iría:
            // mailService.enviar(notificacion.getEmailDonante(), "Comprobante de Donación", 
            //                    notificacion.obtenerMensajeEmail());

            return true;
        } catch (Exception e) {
            logger.error("❌ Error enviando notificación: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Envía notificación al receptor
     */
    public boolean enviarNotificacionReceptor(NotificacionDonacion notificacion) {
        try {
            logger.info("📨 NOTIFICACIÓN AL RECEPTOR");
            logger.info("================================");
            logger.info("Para: {} ({})", notificacion.getNombreReceptor(), notificacion.getEmailReceptor());
            logger.info("Asunto: Donación Recibida - Ticket {}", notificacion.getNumeroTicket());
            logger.info("================================");
            logger.info(notificacion.obtenerMensajeReceptor());
            logger.info("================================\n");

            // En producción:
            // mailService.enviar(notificacion.getEmailReceptor(), "Donación Recibida", 
            //                    notificacion.obtenerMensajeReceptor());

            return true;
        } catch (Exception e) {
            logger.error("❌ Error notificando receptor: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Envía SMS (simulado en desarrollo)
     */
    public boolean enviarSMS(String telefono, NotificacionDonacion notificacion) {
        try {
            logger.info("📱 SMS ENVIADO");
            logger.info("Para: {}", telefono);
            logger.info("Mensaje: {}", notificacion.obtenerMensajeSMS());
            logger.info("");

            // En producción:
            // smsService.enviar(telefono, notificacion.obtenerMensajeSMS());

            return true;
        } catch (Exception e) {
            logger.error("❌ Error enviando SMS: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Genera un recibo/comprobante en formato texto
     */
    public String generarComprobante(NotificacionDonacion notificacion) {
        return notificacion.obtenerMensajeEmail();
    }
}
