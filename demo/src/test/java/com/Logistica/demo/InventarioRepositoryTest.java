package com.Logistica.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.Logistica.demo.model.CentroAcopio;
import com.Logistica.demo.model.Inventario;
import com.Logistica.demo.repository.CentroAcopioRepository;
import com.Logistica.demo.repository.InventarioRepository;

@SpringBootTest
public class InventarioRepositoryTest {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private CentroAcopioRepository centroRepository;

    @Test
    public void testCrearInventario() {
        inventarioRepository.deleteAll();
        centroRepository.deleteAll();

        CentroAcopio centro = new CentroAcopio();
        centro.setNombre("Centro Test");
        centroRepository.save(centro);

        Inventario item = new Inventario();
        item.setRecurso("Alimentos");
        item.setCantidad(100);
        item.setUnidadMedida("Kilos");
        item.setCentroAcopio(centro);

        Inventario guardado = inventarioRepository.save(item);

        assertNotNull(guardado.getId());
        assertEquals("Alimentos", guardado.getRecurso());
        assertEquals(100, guardado.getCantidad());
    }

    @Test
    public void testListarInventarios() {
        CentroAcopio centro = new CentroAcopio();
        centro.setNombre("Centro");
        centroRepository.save(centro);

        Inventario i1 = new Inventario();
        i1.setRecurso("Ropa");
        i1.setCantidad(50);
        i1.setCentroAcopio(centro);
        inventarioRepository.save(i1);

        Inventario i2 = new Inventario();
        i2.setRecurso("Medicinas");
        i2.setCantidad(200);
        i2.setCentroAcopio(centro);
        inventarioRepository.save(i2);

        long count = inventarioRepository.count();
        assertNotNull(count);
    }

    @Test
    public void testInventarioMultiplesCentros() {
        CentroAcopio c1 = new CentroAcopio();
        c1.setNombre("Centro 1");
        centroRepository.save(c1);

        CentroAcopio c2 = new CentroAcopio();
        c2.setNombre("Centro 2");
        centroRepository.save(c2);

        Inventario inv1 = new Inventario();
        inv1.setRecurso("Alimentos");
        inv1.setCantidad(100);
        inv1.setCentroAcopio(c1);
        inventarioRepository.save(inv1);

        Inventario inv2 = new Inventario();
        inv2.setRecurso("Ropa");
        inv2.setCantidad(50);
        inv2.setCentroAcopio(c2);
        inventarioRepository.save(inv2);

        long count = inventarioRepository.count();
        assertNotNull(count);
    }

    @Test
    public void testActualizarCantidadInventario() {
        CentroAcopio centro = new CentroAcopio();
        centroRepository.save(centro);

        Inventario item = new Inventario();
        item.setRecurso("Agua");
        item.setCantidad(1000);
        item.setCentroAcopio(centro);
        Inventario guardado = inventarioRepository.save(item);

        guardado.setCantidad(800);
        Inventario actualizado = inventarioRepository.save(guardado);

        assertNotNull(actualizado);
        assertEquals(800, actualizado.getCantidad());
    }
}
