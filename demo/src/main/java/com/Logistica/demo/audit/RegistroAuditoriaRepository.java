package com.Logistica.demo.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para registros de auditoría
 */
@Repository
public interface RegistroAuditoriaRepository extends JpaRepository<RegistroAuditoria, Long> {
    
    /**
     * Obtiene registros por tipo de recurso
     */
    List<RegistroAuditoria> findByTipoRecursoOrderByTimestampDesc(String tipoRecurso);
    
    /**
     * Obtiene registros por tipo y ID de recurso
     */
    List<RegistroAuditoria> findByTipoRecursoAndIdRecursoOrderByTimestampDesc(String tipo, Long id);
    
    /**
     * Obtiene registros por usuario
     */
    List<RegistroAuditoria> findByUsuarioOrderByTimestampDesc(String usuario);
    
    /**
     * Obtiene registros por acción
     */
    List<RegistroAuditoria> findByAccionOrderByTimestampDesc(String accion);
    
    /**
     * Obtiene registros en rango de fechas
     */
    List<RegistroAuditoria> findByTimestampBetweenOrderByTimestampDesc(
        LocalDateTime inicio, LocalDateTime fin
    );
    
    /**
     * Obtiene por número de ticket
     */
    RegistroAuditoria findByNumeroTicket(String numeroTicket);
}
