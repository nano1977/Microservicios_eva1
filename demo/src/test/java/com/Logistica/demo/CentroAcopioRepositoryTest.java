package com.Logistica.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.Logistica.demo.model.CentroAcopio;
import com.Logistica.demo.repository.CentroAcopioRepository;

@SpringBootTest
public class CentroAcopioRepositoryTest {

    @Autowired
    private CentroAcopioRepository centroRepository;

    @Test
    public void testCrearCentroAcopio() {
        CentroAcopio centro = new CentroAcopio();
        centro.setNombre("Centro Santiago");
        centro.setUbicacion("Santiago, Chile");
        centro.setContacto("+56912345678");

        CentroAcopio guardado = centroRepository.save(centro);

        assertNotNull(guardado.getId());
        assertEquals("Centro Santiago", guardado.getNombre());
        assertEquals("Santiago, Chile", guardado.getUbicacion());
    }

    @Test
    public void testListarCentros() {
        CentroAcopio c1 = new CentroAcopio();
        c1.setNombre("Centro 1");
        c1.setUbicacion("Región 1");
        centroRepository.save(c1);

        CentroAcopio c2 = new CentroAcopio();
        c2.setNombre("Centro 2");
        c2.setUbicacion("Región 2");
        centroRepository.save(c2);

        long count = centroRepository.count();
        assertNotNull(count);
    }

    @Test
    public void testGuardarMultiplesCentros() {
        for (int i = 1; i <= 3; i++) {
            CentroAcopio centro = new CentroAcopio();
            centro.setNombre("Centro " + i);
            centro.setUbicacion("Ubicación " + i);
            centroRepository.save(centro);
        }

        long count = centroRepository.count();
        assertNotNull(count);
    }

    @Test
    public void testActualizarCentro() {
        CentroAcopio centro = new CentroAcopio();
        centro.setNombre("Original");
        centro.setUbicacion("Concepción");
        CentroAcopio guardado = centroRepository.save(centro);

        guardado.setNombre("Actualizado");
        CentroAcopio actualizado = centroRepository.save(guardado);
        
        assertNotNull(actualizado.getId());
        assertEquals("Actualizado", actualizado.getNombre());
    }
}
