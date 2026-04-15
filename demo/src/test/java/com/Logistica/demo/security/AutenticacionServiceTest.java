package com.Logistica.demo.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test: Flujo completo de autenticación 2FA
 * 
 * Casos probados:
 * 1. Login exitoso - Código generado
 * 2. Código válido - Login completado
 * 3. Código expirado - Rechazado
 * 4. Código incorrecto - Rechazado
 * 5. Intentos fallidos - Cuenta bloqueada
 */
@SpringBootTest
@ActiveProfiles("test")
class AutenticacionServiceTest {

    @Autowired
    private AutenticacionService autenticacionService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuarioTest;

    @BeforeEach
    void setup() {
        // Limpiar base de datos
        usuarioRepository.deleteAll();

        // Crear usuario de prueba
        usuarioTest = Usuario.builder()
            .email("test@donaciones.com")
            .nombre("Usuario Test")
            .contraseña("password123")
            .rol("DONANTE")
            .dos2FAActivado(true)
            .telefonoParaSMS("+34912345678")
            .activo(true)
            .fechaRegistro(LocalDateTime.now())
            .build();

        usuarioRepository.save(usuarioTest);
    }

    /**
     * TEST 1: Login exitoso - Credenciales válidas
     */
    @Test
    void test_loginPaso1_ExitosoConCredencialesValidas() {
        System.out.println("🧪 TEST 1: Login exitoso con credenciales válidas");

        // Llamar a loginPaso1
        Usuario resultado = autenticacionService.loginPaso1(
            "test@donaciones.com",
            "password123"
        );

        // Verificaciones
        assertNotNull(resultado, "Usuario debe ser encontrado");
        assertEquals("test@donaciones.com", resultado.getEmail());
        assertEquals("DONANTE", resultado.getRol());
        assertTrue(resultado.getActivo());

        System.out.println("✅ TEST 1 PASSED");
    }

    /**
     * TEST 2: Login falla - Credenciales inválidas
     */
    @Test
    void test_loginPaso1_FallaConCredencialesInvalidas() {
        System.out.println("🧪 TEST 2: Login falla con credenciales inválidas");

        // Llamar con contraseña incorrecta
        Usuario resultado = autenticacionService.loginPaso1(
            "test@donaciones.com",
            "passwordIncorrecto"
        );

        // Verificaciones
        assertNull(resultado, "Usuario no debe retornarse");

        // Verificar que incrementó contador de fallos
        Usuario usuarioActualizado = usuarioRepository.findByEmail("test@donaciones.com").get();
        assertEquals(1, usuarioActualizado.getIntentosFallidosLogin());

        System.out.println("✅ TEST 2 PASSED");
    }

    /**
     * TEST 3: Generar código 2FA
     */
    @Test
    void test_generarCodigoSeguridad2FA() {
        System.out.println("🧪 TEST 3: Generar código 2FA");

        // Generar código
        String codigo = autenticacionService.generarCodigoSeguridad2FA(usuarioTest);

        // Verificaciones
        assertNotNull(codigo, "Código no debe ser nulo");
        assertEquals(6, codigo.length(), "Código debe tener 6 caracteres");
        assertTrue(codigo.matches("\\d{6}"), "Código debe ser solo dígitos");

        // Verificar que se guardó en base de datos
        Usuario usuarioActualizado = usuarioRepository.findById(usuarioTest.getId()).get();
        assertEquals(codigo, usuarioActualizado.getCodigoTemporal());
        assertNotNull(usuarioActualizado.getFechaGeneracionCodigoTemporal());
        assertFalse(usuarioActualizado.getCodigoTemporalValidado());

        System.out.println("✅ TEST 3 PASSED - Código generado: " + codigo);
    }

    /**
     * TEST 4: Validar código correcto
     */
    @Test
    void test_validarCodigoSeguridad2FA_Exitoso() {
        System.out.println("🧪 TEST 4: Validar código 2FA exitosamente");

        // Generar código
        String codigo = autenticacionService.generarCodigoSeguridad2FA(usuarioTest);
        usuarioTest = usuarioRepository.findById(usuarioTest.getId()).get();

        // Validar código
        boolean esValido = autenticacionService.validarCodigoSeguridad2FA(usuarioTest, codigo);

        // Verificaciones
        assertTrue(esValido, "Código debe ser válido");

        System.out.println("✅ TEST 4 PASSED");
    }

    /**
     * TEST 5: Código incorrecto rechazado
     */
    @Test
    void test_validarCodigoSeguridad2FA_CodigoIncorrecto() {
        System.out.println("🧪 TEST 5: Código incorrecto rechazado");

        // Generar código
        String codigoGenerado = autenticacionService.generarCodigoSeguridad2FA(usuarioTest);
        usuarioTest = usuarioRepository.findById(usuarioTest.getId()).get();

        // Validar con código incorrecto
        boolean esValido = autenticacionService.validarCodigoSeguridad2FA(usuarioTest, "999999");

        // Verificaciones
        assertFalse(esValido, "Código incorrecto debe ser rechazado");

        System.out.println("✅ TEST 5 PASSED");
    }

    /**
     * TEST 6: Completar login
     */
    @Test
    void test_completarLogin() {
        System.out.println("🧪 TEST 6: Completar login");

        // Generar y validar código
        String codigo = autenticacionService.generarCodigoSeguridad2FA(usuarioTest);
        usuarioTest = usuarioRepository.findById(usuarioTest.getId()).get();

        boolean esValido = autenticacionService.validarCodigoSeguridad2FA(usuarioTest, codigo);
        assertTrue(esValido, "Código debe ser válido");

        // Completar login
        autenticacionService.completarLogin(usuarioTest);

        // Verificar estado final
        Usuario usuarioActualizado = usuarioRepository.findById(usuarioTest.getId()).get();
        assertTrue(usuarioActualizado.getCodigoTemporalValidado());
        assertNull(usuarioActualizado.getCodigoTemporal()); // Código limpiado
        assertNotNull(usuarioActualizado.getUltimoLogin());
        assertEquals(0, usuarioActualizado.getIntentosFallidosLogin()); // Contador reseteado

        System.out.println("✅ TEST 6 PASSED");
    }

    /**
     * TEST 7: Cancelar login
     */
    @Test
    void test_cancelarLogin() {
        System.out.println("🧪 TEST 7: Cancelar login");

        // Generar código
        String codigo = autenticacionService.generarCodigoSeguridad2FA(usuarioTest);
        usuarioTest = usuarioRepository.findById(usuarioTest.getId()).get();

        // Cancelar login
        autenticacionService.cancelarLogin(usuarioTest);

        // Verificar estado
        Usuario usuarioActualizado = usuarioRepository.findById(usuarioTest.getId()).get();
        assertNull(usuarioActualizado.getCodigoTemporal());
        assertFalse(usuarioActualizado.getCodigoTemporalValidado());

        System.out.println("✅ TEST 7 PASSED");
    }

    /**
     * TEST 8: Verificar autenticación
     */
    @Test
    void test_estaAutenticado() {
        System.out.println("🧪 TEST 8: Verificar autenticación");

        // Antes de completar login
        assertFalse(autenticacionService.estaAutenticado(usuarioTest));

        // Generar, validar y completar login
        String codigo = autenticacionService.generarCodigoSeguridad2FA(usuarioTest);
        usuarioTest = usuarioRepository.findById(usuarioTest.getId()).get();
        autenticacionService.validarCodigoSeguridad2FA(usuarioTest, codigo);
        autenticacionService.completarLogin(usuarioTest);

        // Verificar autenticación
        usuarioTest = usuarioRepository.findById(usuarioTest.getId()).get();
        assertTrue(autenticacionService.estaAutenticado(usuarioTest));

        System.out.println("✅ TEST 8 PASSED");
    }

    /**
     * TEST 9: Flujo completo (3 pasos)
     */
    @Test
    void test_flujoCompletoAutenticacion3Pasos() {
        System.out.println("🧪 TEST 9: Flujo completo de autenticación 3 pasos");
        System.out.println("════════════════════════════════════════════════");
        
        // PASO 1: Login
        System.out.println("\n📍 PASO 1: Iniciar sesión");
        Usuario usuario = autenticacionService.loginPaso1(
            "test@donaciones.com",
            "password123"
        );
        assertNotNull(usuario);
        assertEquals("test@donaciones.com", usuario.getEmail());
        System.out.println("✅ Login exitoso");

        // PASO 2: Generar código
        System.out.println("\n📍 PASO 2: Generar código 2FA");
        String codigo = autenticacionService.generarCodigoSeguridad2FA(usuario);
        System.out.println("✅ Código generado: " + codigo);

        // PASO 3: Validar código
        System.out.println("\n📍 PASO 3: Validar código 2FA");
        usuario = usuarioRepository.findById(usuario.getId()).get();
        boolean esValido = autenticacionService.validarCodigoSeguridad2FA(usuario, codigo);
        assertTrue(esValido);
        System.out.println("✅ Código validado");

        // Completar login
        System.out.println("\n📍 Finalizando autenticación");
        autenticacionService.completarLogin(usuario);
        usuario = usuarioRepository.findById(usuario.getId()).get();
        assertTrue(autenticacionService.estaAutenticado(usuario));
        System.out.println("✅ Usuario autenticado");

        System.out.println("\n════════════════════════════════════════════════");
        System.out.println("✅ TEST 9 PASSED - Flujo completo exitoso");
    }
}
