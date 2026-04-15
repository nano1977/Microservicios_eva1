package com.Logistica.demo.security;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Servicio de autenticación con 2FA
 * Implementa:
 * 1. Login con email + contraseña
 * 2. Generación de código 2FA temporal
 * 3. Validación de código 2FA
 */
@Service
public class AutenticacionService {

    private static final Logger logger = LoggerFactory.getLogger(AutenticacionService.class);
    private static final int MINUTOS_EXPIRACION_CODIGO = 5; // Código válido por 5 minutos
    private static final int INTENTOS_MAXIMOS_FALLIDA = 3;

    private final UsuarioRepository usuarioRepository;

    public AutenticacionService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * PASO 1: Login - Verifica email y contraseña
     * @return Usuario si credenciales son válidas, null si no
     */
    public Usuario loginPaso1(String email, String contraseña) {
        logger.info("🔐 PASO 1: Verificando credenciales para {}", email);

        // 1. Obtener usuario
        var usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            logger.warn("❌ Usuario no encontrado: {}", email);
            return null;
        }

        Usuario usuario = usuarioOpt.get();

        // 2. Verificar contraseña
        if (!usuario.getContraseña().equals(contraseña)) { // En producción: comparar HASH
            usuario.setIntentosFallidosLogin(usuario.getIntentosFallidosLogin() + 1);
            usuarioRepository.save(usuario);
            logger.warn("❌ Contraseña incorrecta para {}", email);
            return null;
        }

        // 3. Verificar si está activo
        if (!usuario.getActivo()) {
            logger.warn("❌ Usuario inactivo: {}", email);
            return null;
        }

        logger.info("✅ Credenciales válidas para {}", email);
        return usuario;
    }

    /**
     * PASO 2: Generar código 2FA temporal
     * En producción: enviar por SMS real
     */
    public String generarCodigoSeguridad2FA(Usuario usuario) {
        logger.info("🔑 PASO 2: Generando código 2FA para {}", usuario.getEmail());

        // Generar código aleatorio de 6 dígitos
        String codigo = String.format("%06d", new Random().nextInt(999999));
        
        usuario.setCodigoTemporal(codigo);
        usuario.setFechaGeneracionCodigoTemporal(LocalDateTime.now());
        usuario.setCodigoTemporalValidado(false);
        usuarioRepository.save(usuario);

        logger.info("📨 Código 2FA generado: {}", codigo);
        logger.info("📱 En producción, enviar por SMS a: {}", usuario.getTelefonoParaSMS());
        logger.info("⏰ Código válido por {} minutos", MINUTOS_EXPIRACION_CODIGO);

        return codigo; // En desarrollo: retorna el código (en producción: NO)
    }

    /**
     * PASO 3: Validar código 2FA
     * @return true si el código es correcto y no ha expirado
     */
    public boolean validarCodigoSeguridad2FA(Usuario usuario, String codigoIngresado) {
        logger.info("✔️ PASO 3: Validando código 2FA para {}", usuario.getEmail());

        // 1. Verificar que código no sea nulo
        if (usuario.getCodigoTemporal() == null) {
            logger.warn("❌ No hay código temporal generado");
            return false;
        }

        // 2. Verificar que código coincida
        if (!usuario.getCodigoTemporal().equals(codigoIngresado)) {
            logger.warn("❌ Código incorrecto");
            return false;
        }

        // 3. Verificar que no haya expirado (5 minutos)
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime expiracion = usuario.getFechaGeneracionCodigoTemporal()
            .plusMinutes(MINUTOS_EXPIRACION_CODIGO);
        
        if (ahora.isAfter(expiracion)) {
            logger.warn("❌ Código expirado");
            return false;
        }

        logger.info("✅ Código 2FA válido");
        return true;
    }

    /**
     * Finalizar login exitosamente
     */
    public void completarLogin(Usuario usuario) {
        logger.info("🎉 LOGIN COMPLETADO para {}", usuario.getEmail());
        
        usuario.setCodigoTemporal(null);
        usuario.setFechaGeneracionCodigoTemporal(null);
        usuario.setCodigoTemporalValidado(true);
        usuario.setUltimoLogin(LocalDateTime.now());
        usuario.setIntentosFallidosLogin(0); // Resetear intentos fallidos
        usuarioRepository.save(usuario);

        logger.info("✅ Usuario {} autenticado exitosamente", usuario.getEmail());
    }

    /**
     * Cancelar login y limpiar códigos
     */
    public void cancelarLogin(Usuario usuario) {
        logger.info("⛔ Login cancelado para {}", usuario.getEmail());
        
        usuario.setCodigoTemporal(null);
        usuario.setFechaGeneracionCodigoTemporal(null);
        usuario.setCodigoTemporalValidado(false);
        usuarioRepository.save(usuario);
    }

    /**
     * Verificar si usuario está correctamente autenticado
     */
    public boolean estaAutenticado(Usuario usuario) {
        return usuario != null 
            && usuario.getCodigoTemporalValidado() 
            && usuario.getActivo();
    }
}
