package com.Logistica.demo.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Servicio que gestiona la auditoría de todas las acciones en la aplicación
 * Registra quién, qué, cuándo y detalles de cada operación
 */
@Service
@Transactional
public class AuditoriaService {

    @Autowired
    private RegistroAuditoriaRepository auditoriaRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * Registra una acción de auditoría
     * @param accion CREAR, ACTUALIZAR, ELIMINAR, TRANSFERIR
     * @param tipoRecurso tipo de entidad
     * @param idRecurso ID de la entidad
     * @param usuario quién realizó la acción
     * @param rol rol del usuario
     * @param detalles descripción completa
     */
    public RegistroAuditoria registrarAccion(String accion, String tipoRecurso, Long idRecurso,
                                             String usuario, String rol, String detalles) {
        RegistroAuditoria registro = RegistroAuditoria.builder()
            .accion(accion)
            .tipoRecurso(tipoRecurso)
            .idRecurso(idRecurso)
            .usuario(usuario)
            .rol(rol)
            .timestamp(LocalDateTime.now())
            .detalles(detalles)
            .numeroTicket(generarNumeroTicket())
            .notificacionEnviada(false)
            .build();

        return auditoriaRepository.save(registro);
    }

    /**
     * Registra una donación recibida
     * @param centroDonacion centro que la recibe
     * @param nombreDonante nombre de quien donó
     * @param emailDonante email del donante
     * @param nombreReceptor nombre de quien recibe
     * @param emailReceptor email del receptor
     * @param detalles descripción de la donación
     */
    public RegistroAuditoria registrarDonacion(String centroDonacion, String nombreDonante,
                                               String emailDonante, String nombreReceptor,
                                               String emailReceptor, String detalles) {
        RegistroAuditoria registro = RegistroAuditoria.builder()
            .accion("DONACION_RECIBIDA")
            .tipoRecurso("Donacion")
            .usuario(nombreReceptor)
            .rol("Receptor")
            .timestamp(LocalDateTime.now())
            .detalles(detalles)
            .centroDonacion(centroDonacion)
            .nombreDonante(nombreDonante)
            .emailDonante(emailDonante)
            .nombreReceptor(nombreReceptor)
            .emailReceptor(emailReceptor)
            .numeroTicket(generarNumeroTicket())
            .notificacionEnviada(false)
            .build();

        return auditoriaRepository.save(registro);
    }

    /**
     * Genera número de ticket único
     * Formato: DON-YYYYMMDD-XXXXX
     * Ej: DON-20260415-00001
     */
    private String generarNumeroTicket() {
        LocalDateTime ahora = LocalDateTime.now();
        String fecha = ahora.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String timestamp = ahora.format(formatter);
        return String.format("DON-%s-%s", fecha, timestamp.substring(8));
    }

    /**
     * Obtiene registros de auditoría por tipo de recurso
     */
    public List<RegistroAuditoria> obtenerPorTipoRecurso(String tipo) {
        return auditoriaRepository.findByTipoRecursoOrderByTimestampDesc(tipo);
    }

    /**
     * Obtiene registros de auditoría por ID de recurso
     */
    public List<RegistroAuditoria> obtenerPorRecurso(String tipo, Long id) {
        return auditoriaRepository.findByTipoRecursoAndIdRecursoOrderByTimestampDesc(tipo, id);
    }

    /**
     * Obtiene registros de auditoría por usuario
     */
    public List<RegistroAuditoria> obtenerPorUsuario(String usuario) {
        return auditoriaRepository.findByUsuarioOrderByTimestampDesc(usuario);
    }

    /**
     * Obtiene registro por número de ticket
     */
    public RegistroAuditoria obtenerPorTicket(String numeroTicket) {
        return auditoriaRepository.findByNumeroTicket(numeroTicket);
    }

    /**
     * Marca notificación como enviada
     */
    public void marcarNotificacionEnviada(Long auditoriaId) {
        auditoriaRepository.findById(auditoriaId).ifPresent(registro -> {
            registro.setNotificacionEnviada(true);
            registro.setFechaNotificacion(LocalDateTime.now());
            auditoriaRepository.save(registro);
        });
    }
}
