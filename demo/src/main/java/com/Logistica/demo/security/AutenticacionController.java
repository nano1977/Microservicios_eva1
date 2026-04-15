package com.Logistica.demo.security;

import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *🔐 Controlador de Autenticación con 2FA (Dos Factores)
 * 
 * FLUJO DE AUTENTICACIÓN (3 Pasos):
 * 
 * PASO 1: POST /api/auth/login
 *    Input: { "email": "user@example.com", "contraseña": "pass123" }
 *    Output: { "codigoTemporal": "123456", "mensaje": "Código enviado a SMS" }
 * 
 * PASO 2: [Usuario recibe código por SMS/email]
 * 
 * PASO 3: POST /api/auth/verificar-codigo-2fa
 *    Input: { "usuarioId": 1, "codigoIngresado": "123456" }
 *    Output: { "exitoso": true, "token": "jwt_token...", "mensaje": "Login exitoso" }
 */
@RestController
@RequestMapping("/api/auth")
public class AutenticacionController {

    private static final Logger logger = LoggerFactory.getLogger(AutenticacionController.class);

    private final AutenticacionService autenticacionService;
    private final UsuarioRepository usuarioRepository;

    public AutenticacionController(AutenticacionService autenticacionService, 
                                    UsuarioRepository usuarioRepository) {
        this.autenticacionService = autenticacionService;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * ═══════════════════════════════════════
     * PASO 1: Login - Verificar Credenciales
     * ═══════════════════════════════════════
     * 
     * POST /api/auth/login
     * 
     * Request:
     * {
     *   "email": "juan@donaciones.com",
     *   "contraseña": "miContraseña123"
     * }
     * 
     * Response (200 OK):
     * {
     *   "exitoso": true,
     *   "mensaje": "Credenciales válidas. Código 2FA generado.",
     *   "usuarioId": 1,
     *   "email": "juan@donaciones.com",
     *   "nombre": "Juan Pérez",
     *   "rol": "DONANTE",
     *   "codigoTemporal": "654321",
     *   "instrucciones": "Ingresa el código recibido por SMS en los próximos 5 minutos"
     * }
     * 
     * Response (401 Unauthorized):
     * {
     *   "exitoso": false,
     *   "mensaje": "Email o contraseña incorrectos"
     * }
     */
    @PostMapping("/login")
    public RespuestaLoginDTO loginPaso1(@RequestBody SolicitudLoginDTO solicitud) {
        logger.info("╔══════════════════════════════════════════════════════╗");
        logger.info("║ 🔐 PASO 1: LOGIN - VERIFICAR CREDENCIALES             ║");
        logger.info("╚══════════════════════════════════════════════════════╝");

        try {
            // Validaciones básicas
            if (solicitud.getEmail() == null || solicitud.getEmail().trim().isEmpty()) {
                logger.warn("❌ Email vacío");
                return RespuestaLoginDTO.builder()
                    .exitoso(false)
                    .mensaje("email y contraseña son requeridos")
                    .build();
            }

            // PASO 1: Verificar credenciales
            Usuario usuario = autenticacionService.loginPaso1(
                solicitud.getEmail(), 
                solicitud.getContraseña()
            );

            if (usuario == null) {
                logger.warn("❌ Credenciales inválidas");
                return RespuestaLoginDTO.builder()
                    .exitoso(false)
                    .mensaje("Email o contraseña incorrectos")
                    .build();
            }

            // Verificar si 2FA está activado
            if (!usuario.getDos2FAActivado()) {
                logger.warn("⚠️  2FA desactivado para usuario: {}", usuario.getEmail());
                return RespuestaLoginDTO.builder()
                    .exitoso(true)
                    .mensaje("Login exitoso (2FA desactivado)")
                    .usuarioId(usuario.getId())
                    .email(usuario.getEmail())
                    .nombre(usuario.getNombre())
                    .rol(usuario.getRol())
                    .instrucciones("Acceso concedido sin verificación 2FA")
                    .build();
            }

            // PASO 2: Generar código 2FA temporal
            String codigo = autenticacionService.generarCodigoSeguridad2FA(usuario);

            logger.info("✅ Credenciales válidas para: {}", usuario.getEmail());
            logger.info("📱 Código 2FA generado: {}", codigo);

            return RespuestaLoginDTO.builder()
                .exitoso(true)
                .mensaje("Credenciales válidas. Código 2FA generado.")
                .usuarioId(usuario.getId())
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .rol(usuario.getRol())
                .codigoTemporal(codigo) // En DESARROLLO solamente (mostrar para testing)
                .instrucciones("✅ Se ha enviado un código a tu SMS.\n" +
                               "📱 El código es válido por 5 minutos.\n" +
                               "🔑 Ingresa el código en el siguiente paso.")
                .build();

        } catch (Exception e) {
            logger.error("❌ Error en login: {}", e.getMessage(), e);
            return RespuestaLoginDTO.builder()
                .exitoso(false)
                .mensaje("Error al procesar login: " + e.getMessage())
                .build();
        }
    }

    /**
     * ═══════════════════════════════════════════════════════════
     * PASO 3: Validar Código 2FA y Completar Autenticación
     * ═══════════════════════════════════════════════════════════
     * 
     * POST /api/auth/verificar-codigo-2fa
     * 
     * Request:
     * {
     *   "usuarioId": 1,
     *   "codigoIngresado": "654321"
     * }
     * 
     * Response (200 OK):
     * {
     *   "exitoso": true,
     *   "mensaje": "Autenticación completada exitosamente",
     *   "usuarioId": 1,
     *   "email": "juan@donaciones.com",
     *   "nombre": "Juan Pérez",
     *   "rol": "DONANTE",
     *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
     * }
     * 
     * Response (401 Unauthorized):
     * {
     *   "exitoso": false,
     *   "mensaje": "Código 2FA inválido o expirado"
     * }
     */
    @PostMapping("/verificar-codigo-2fa")
    public RespuestaAutenticacionDTO verificarCodigoP3(@RequestBody ValidacionCodigoDTO solicitud) {
        logger.info("╔══════════════════════════════════════════════════════╗");
        logger.info("║ 🔑 PASO 3: VALIDAR CÓDIGO 2FA                        ║");
        logger.info("╚══════════════════════════════════════════════════════╝");

        try {
            // Obtener usuario
            var usuarioOpt = usuarioRepository.findById(solicitud.getUsuarioId());
            if (usuarioOpt.isEmpty()) {
                logger.warn("❌ Usuario no encontrado: {}", solicitud.getUsuarioId());
                return RespuestaAutenticacionDTO.builder()
                    .exitoso(false)
                    .mensaje("Usuario no encontrado")
                    .build();
            }

            Usuario usuario = usuarioOpt.get();

            // PASO 3: Validar código
            boolean codigoValido = autenticacionService.validarCodigoSeguridad2FA(
                usuario, 
                solicitud.getCodigoIngresado()
            );

            if (!codigoValido) {
                logger.warn("❌ Código 2FA inválido o expirado para: {}", usuario.getEmail());
                return RespuestaAutenticacionDTO.builder()
                    .exitoso(false)
                    .mensaje("Código 2FA inválido o expirado")
                    .build();
            }

            // Completar login
            autenticacionService.completarLogin(usuario);

            // Generar token JWT (en producción: usar JWT real)
            String token = generarTokenJWT(usuario);

            logger.info("✅ Usuario autenticado exitosamente: {}", usuario.getEmail());
            logger.info("🎉 Login completado - Token generado");

            return RespuestaAutenticacionDTO.builder()
                .exitoso(true)
                .mensaje("Autenticación completada exitosamente ✅")
                .usuarioId(usuario.getId())
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .rol(usuario.getRol())
                .token(token)
                .build();

        } catch (Exception e) {
            logger.error("❌ Error al validar código 2FA: {}", e.getMessage(), e);
            return RespuestaAutenticacionDTO.builder()
                .exitoso(false)
                .mensaje("Error: " + e.getMessage())
                .build();
        }
    }

    /**
     * GET /api/auth/logout
     * Cierra la sesión del usuario
     */
    @PostMapping("/logout")
    public RespuestaAutenticacionDTO logout(@RequestParam Long usuarioId) {
        logger.info("🚪 Logout para usuario: {}", usuarioId);

        var usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            autenticacionService.cancelarLogin(usuario);
            logger.info("✅ Logout exitoso");
        }

        return RespuestaAutenticacionDTO.builder()
            .exitoso(true)
            .mensaje("Sesión cerrada correctamente")
            .build();
    }

    // ═══════════════════════════════════════════════════════════════
    // MÉTODO AUXILIAR: Generar token JWT
    // ═══════════════════════════════════════════════════════════════
    private String generarTokenJWT(Usuario usuario) {
        // En producción: usar biblioteca JWT (jjwt, auth0-java-jwt)
        // Por ahora: token simple basado en email + timestamp
        String token = "Bearer-" + usuario.getEmail() + "-" + 
                       System.currentTimeMillis() + 
                       "-" + usuario.getRol();
        logger.info("🔓 Token JWT generado: {}", token);
        return token;
    }
}
