package com.Logistica.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Logistica.demo.audit.AuditoriaService;
import com.Logistica.demo.audit.RegistroAuditoria;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auditoria")
@Tag(name = "Auditoria", description = "API para consultar registros de auditoria")
public class AuditoriaController {

    @Autowired
    private AuditoriaService auditoriaService;

    @GetMapping
    @Operation(summary = "Lista todos los registros de auditoria")
    public ResponseEntity<List<RegistroAuditoria>> listarAuditoria() {
        return ResponseEntity.ok(auditoriaService.obtenerTodos());
    }
}
