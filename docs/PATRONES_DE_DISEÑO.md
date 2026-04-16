# 📐 Análisis de Patrones de Diseño - Gestión de Logística
## Proyecto: Gestión de Logística y Distribución

**Fecha**: 13 de abril de 2026  
**Versión**: 1.0  
**Equipo**: Desarrollo Microservicios

---

## 📋 Tabla de Contenidos
1. [Caso de Uso](#caso-de-uso)
2. [Requerimientos Analizados](#requerimientos-analizados)
3. [Patrones Seleccionados](#patrones-seleccionados)
4. [Justificación Técnica](#justificación-técnica)
5. [Propuesta de Implementación](#propuesta-de-implementación)

---

## 🎯 Caso de Uso

**Contexto**: Plataforma de Gestión de Logística y Distribución para gestión de transporte y distribución de recursos.

**Problema Principal**: 
- Gestionar múltiples entidades interrelacionadas (Vehículos, Centros de Acopio, Inventario)
- Implementar lógica de negocio compleja que varía según el tipo de recurso
- Garantizar escalabilidad y mantenibilidad conforme crece el sistema

---

## ✅ Requerimientos Analizados

### Requerimientos Funcionales
- ✅ Crear, listar, actualizar y eliminar vehículos
- ✅ Administrar estados de vehículos (Disponible, En Ruta, Mantenimiento)
- ✅ Gestionar centros de acopio y su capacidad
- ✅ Controlar inventario por centro de acopio
- ✅ Asociar y desasociar recursos

### Requerimientos No Funcionales
- 🔒 **Mantenibilidad**: Código modular y fácil de extender
- 🚀 **Escalabilidad**: Agregar nuevos tipos de entidades y comportamientos
- 📦 **Reutilización**: Evitar duplicación de lógica de negocio
- 🧪 **Testabilidad**: Código desacoplado y fácil de probar
- 🛡️ **Consistencia de Datos**: Validaciones complejas según contexto

---

## 🏆 Patrones Seleccionados (Lista Priorizada)

### **PATRÓN 1: Strategy Pattern** ⭐⭐⭐ (MÁXIMA PRIORIDAD)

#### 📝 Descripción Breve
El patrón **Strategy** define una familia de algoritmos, encapsúlalos y hazlos intercambiables. Permite seleccionar el algoritmo en tiempo de ejecución según el contexto específico.

#### 🎯 Justificación Alineada con Requerimientos

**✅ Requerimientos Funcionales Cubiertos**:
- Implementar diferentes estrategias de validación para cada tipo de recurso (Vehículo, CentroAcopio, Inventario)
- Permitir diferentes cálculos de capacidad según el tipo de vehículo
- Implementar estrategias de asignación de recursos basadas en disponibilidad

**✅ Requerimientos No Funcionales Cubiertos**:
- **Mantenibilidad**: Cada estrategia está en su propia clase, fácil de modificar sin afectar otras
- **Escalabilidad**: Agregar nuevas estrategias de validación o cálculo sin modificar código existente
- **Testabilidad**: Cada estrategia se prueba independientemente
- **Reutilización**: Múltiples contextos pueden usar la misma estrategia

#### 💡 Casos de Uso Aplicables
```
1. Validación Flexible
   - ValidadorVehículo: Valida patente, modelo, chofer
   - ValidadorCentroAcopio: Valida ubicación, capacidad
   - ValidadorInventario: Valida cantidad, unidades

2. Cálculo de Capacidad
   - CalculadorCapacidadVehiculoPesado: Límite por toneladas
   - CalculadorCapacidadEmpaques: Límite por número de cajas
   - CalculadorCapacidadTemperatura: Límite según refrigeración

3. Asignación de Recursos
   - EstrategiaAsignacionPorDisponibilidad
   - EstrategiaAsignacionPorProximidad
   - EstrategiaAsignacionPorCapacidad
```

#### 🔧 Ejemplo de Implementación en el Proyecto

```java
// Interfaz Strategy
public interface EstrategiaValidacion {
    ResultadoValidacion validar(Object recurso);
}

// Estrategias Concretas
@Component
public class ValidadorVehiculo implements EstrategiaValidacion {
    @Override
    public ResultadoValidacion validar(Object recurso) {
        Vehiculo v = (Vehiculo) recurso;
        // Lógica de validación específica para vehículos
        if (v.getPatente() == null || v.getCapacidadCarga() <= 0) {
            return new ResultadoValidacion(false, "Datos incompletos");
        }
        return new ResultadoValidacion(true, "Válido");
    }
}

// Contexto que usa la estrategia
@Service
public class LogisticaService {
    private Map<Class<?>, EstrategiaValidacion> validadores;
    
    public void guardarRecurso(Object recurso) {
        EstrategiaValidacion validador = validadores.get(recurso.getClass());
        ResultadoValidacion resultado = validador.validar(recurso);
        if (!resultado.esValido()) {
            throw new IllegalArgumentException(resultado.getMensaje());
        }
        // Guardar recurso validado
    }
}
```

---

### **PATRÓN 2: Factory Pattern + Service Layer** ⭐⭐ (ALTA PRIORIDAD)

#### 📝 Descripción Breve
El patrón **Factory** proporciona una interfaz para crear familias de objetos sin especificar sus clases concretas. Combinado con **Service Layer** encapsula toda la lógica de negocio.

#### 🎯 Justificación Alineada con Requerimientos

**✅ Requerimientos Funcionales Cubiertos**:
- Centralizar la creación de entidades complejas (con validaciones y reglas de negocio)
- Encapsular operaciones CRUD complejas (crear vehículo con validaciones, asignar a rutas, etc.)
- Implementar reglas de negocio centralizadas

**✅ Requerimientos No Funcionales Cubiertos**:
- **Mantenibilidad**: Lógica de negocio centralizada en una capa Service
- **Escalabilidad**: Fácil agregar nuevos tipos de recursos y servicios
- **Testabilidad**: Services se prueban sin dependencias de Repository
- **Separación de Responsabilidades**: Controller solo orquesta, Service implementa reglas

#### 💡 Casos de Uso Aplicables
```
1. Creación Controlada de Entidades
   - CreadorVehiculo: Asigna ID único, estado inicial, validaciones
   - CreadorCentroAcopio: Valida ubicación, capacidad máxima
   - CreadorAsignacionRuta: Encuentra vehículo disponible + centro origen/destino

2. Operaciones Complejas
   - AsignarVehiculoAEntraga: Verifica disponibilidad → actualiza estado
   - CalcularRutaOptima: Considera ubicaciones, capacidades, tiempos
   - TransferirInventario: Valida existencias, actualiza múltiples centros
```

#### 🔧 Ejemplo de Implementación en el Proyecto

```java
// Factory Interface
public interface RecursoFactory {
    <T> T crear(T entidad);
}

// Implementación específica
@Service
public class VehiculoFactory implements RecursoFactory {
    @Autowired
    private VehiculoRepository vehiculoRepository;
    
    @Autowired
    private EstrategiaValidacion validador;
    
    @Override
    public <T> T crear(T entidad) {
        Vehiculo vehiculo = (Vehiculo) entidad;
        
        // Validar
        ResultadoValidacion resultado = validador.validar(vehiculo);
        if (!resultado.esValido()) {
            throw new IllegalArgumentException(resultado.getMensaje());
        }
        
        // Aplicar reglas de negocio
        if (vehiculo.getEstado() == null) {
            vehiculo.setEstado("Disponible");
        }
        vehiculo.setFechaRegistro(LocalDateTime.now());
        
        return (T) vehiculoRepository.save(vehiculo);
    }
}

// Service Layer
@Service
public class LogisticaService {
    @Autowired
    private VehiculoFactory vehiculoFactory;
    
    @Autowired
    private CentroAcopioFactory centroFactory;
    
    public Vehiculo crearVehiculo(Vehiculo vehiculo) {
        return vehiculoFactory.crear(vehiculo);
    }
    
    public List<Vehiculo> obtenerVehiculosDisponibles() {
        // Lógica de negocio compleja
        List<Vehiculo> disponibles = vehiculoRepository.findByEstado("Disponible");
        return disponibles.stream()
            .filter(v -> v.getCapacidadCarga() > 0)
            .collect(Collectors.toList());
    }
    
    public boolean asignarVehiculoAEntrega(Long vehiculoId, Long entregarId) {
        // Lógica compleja de asignación
        // ...
    }
}

// Controller actualizado (menor responsabilidad)
@RestController
@RequestMapping("/api/logistica")
public class LogisticaController {
    @Autowired
    private LogisticaService logisticaService;
    
    @PostMapping("/vehiculos")
    public Vehiculo crear(@RequestBody Vehiculo vehiculo) {
        return logisticaService.crearVehiculo(vehiculo);
    }
}
```

---

## 📊 Comparativa de Patrones

| Aspecto | Strategy | Factory + Service |
|--------|----------|------------------|
| **Flexibilidad** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| **Complejidad** | Baja | Media |
| **Escalabilidad** | Alto | Muy Alto |
| **Testabilidad** | Alto | Muy Alto |
| **Curva Aprendizaje** | Baja | Media |
| **Mejor para** | Algoritmos variables | Lógica de negocio |

---

## 🚀 Propuesta de Implementación

### Fase 1: Implementación Strategy Pattern (Semana 1-2)
```
1. Crear interfaces de estrategia
2. Implementar validadores para cada entidad
3. Incorporar estrategias en el Controller
4. Escribir tests unitarios
```

### Fase 2: Implementación Factory + Service (Semana 3-4)
```
1. Crear capa Service intermedia
2. Implementar Factories para cada entidad
3. Migrar lógica del Controller al Service
4. Refactorizar Controllers para usar Services
5. Implementar tests de integración
```

### Fase 3: Optimización y Extensión (Semana 5+)
```
1. Agregar más estrategias según nuevos requisitos
2. Implementar Decorator Pattern para composición
3. Auditoría y logging
4. Caché de estrategias
```

---

## 🎓 Beneficios Esperados

### Corto Plazo
✅ Código más organizado y mantenible  
✅ Reducción de duplicación de código  
✅ Tests más fáciles de escribir  

### Mediano Plazo
✅ Sistema escalable para nuevas entidades  
✅ Fácil agregar nuevas reglas de negocio  
✅ Mejor performance (caching, optimizaciones)  

### Largo Plazo
✅ Arquitectura preparada para crecimiento  
✅ Fácil migración a arquitectura por eventos  
✅ Soporte para múltiples estrategias simultáneamente  

---

## 📚 Patrones Complementarios (Futuros)

Para futuras iteraciones se recomienda considerar:

- **Observer Pattern**: Los cambios de estado de vehículos notifiquen a centros de acopio
- **Decorator Pattern**: Agregar funcionalidades a validadores/estrategias dinámicamente
- **Repository Pattern** (mejorado): Especificaciones complejas de query
- **DTO Pattern**: Separar modelo de persistencia del modelo de transferencia

---

## 📝 Conclusión

Se recomienda implementar **Strategy Pattern** como fundación (alta flexibilidad, bajo riesgo) seguido de **Factory Pattern + Service Layer** para encapsular la lógica de negocio compleja. Esta combinación garantiza:

✅ **Modularidad**: Cada patrón maneja un aspecto específico  
✅ **Escalabilidad**: Fácil extensión sin modificar código existente  
✅ **Testabilidad**: Componentes desacoplados y probables  
✅ **Mantenibilidad**: Código claro y organizado  

---

**Aprobado por**: Equipo de Desarrollo  
**Próximo paso**: Iniciar implementación en Fase 1
