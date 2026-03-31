package com.Logistica.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String recurso; // "Alimento", "Ropa", "Insumos Médicos" [cite: 7]
    private Integer cantidad;
    private String unidadMedida; // "Kilos", "Unidades", "Cajas"
    
    @ManyToOne
    private CentroAcopio centroAcopio; // Relaciona el recurso con un lugar físico [cite: 24]
}