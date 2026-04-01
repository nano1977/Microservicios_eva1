# 🚀 Microservicios de Logística

Sistema de gestión de logística y distribución desarrollado con **Spring Boot 4.0.5** y **Java 21**.

## 📋 Descripción

Aplicación Spring Boot para la gestión de:
- **Centros de Acopio**
- **Inventarios**
- **Vehículos**

Con API REST documentada con **Swagger OpenAPI** y base de datos **H2** persistente.

---

## 🛠️ Requisitos

- **Java 21** o superior
- **Maven 3.9.14** (o superior)
- **Docker** (opcional, para futuras migraciones)

---

## 📦 Base de Datos

### Configuración Actual: H2 (Persistente)

**Archivo:** `src/main/resources/application.properties`

```properties
# H2 Database (Archivo local - Datos persisten)
spring.datasource.url=jdbc:h2:./data/logistica_db
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
```

### Credenciales H2

| Campo | Valor |
|-------|-------|
| **Usuario** | `sa` |
| **Contraseña** | (vacía) |
| **URL JDBC** | `jdbc:h2:./data/logistica_db` |
| **Ubicación datos** | `./data/logistica_db.mv.db` |

### 💾 Persistencia de Datos

✅ Los datos **NO se pierden** al reiniciar la aplicación  
✅ Se guardan en archivos del disco duro: `./data/logistica_db`  
✅ Al ejecutar nuevamente, se cargan automáticamente  

---

## 🚀 Cómo Ejecutar

### Opción 1: Con Maven directo

```bash
cd demo
C:\Users\Usuario\.maven\maven-3.9.14\bin\mvn spring-boot:run
```

### Opción 2: Con script batch (Windows)

```bash
cd demo
run.bat
```

### Opción 3: Compilar y ejecutar

```bash
cd demo
C:\Users\Usuario\.maven\maven-3.9.14\bin\mvn clean compile spring-boot:run
```

---

## 🌐 URLs de Acceso

Una vez que inicie (espera ~30 segundos):

| Recurso | URL |
|---------|-----|
| **Swagger UI (API Docs)** | http://localhost:8080/swagger-ui.html |
| **Documentación OpenAPI** | http://localhost:8080/v3/api-docs |
| **Consola H2** | http://localhost:8080/h2-console |
| **Inicio** | http://localhost:8080 |

### Acceso a Consola H2

1. Abre: http://localhost:8080/h2-console
2. **JDBC URL:** `jdbc:h2:./data/logistica_db`
3. **User Name:** `sa`
4. **Password:** (dejar vacío)
5. Haz clic en **"Connect"**

---

## 📊 Estructura del Proyecto

```
demo/
├── src/
│   ├── main/
│   │   ├── java/com/Logistica/demo/
│   │   │   ├── DemoApplication.java
│   │   │   ├── config/
│   │   │   │   └── OpenApiConfig.java
│   │   │   ├── controller/
│   │   │   │   └── LogisticaController.java
│   │   │   ├── model/
│   │   │   │   ├── CentroAcopio.java
│   │   │   │   ├── Inventario.java
│   │   │   │   └── Vehiculo.java
│   │   │   └── repository/
│   │   │       ├── CentroAcopioRepository.java
│   │   │       ├── InventarioRepository.java
│   │   │       └── VehiculoRepository.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/Logistica/demo/
│           ├── CentroAcopioRepositoryTest.java
│           ├── DemoApplicationTests.java
│           ├── InventarioRepositoryTest.java
│           └── VehiculoRepositoryTest.java
├── pom.xml
├── docker-compose.yml
└── run.bat
```

---

## 🔧 Dependencias Principales

```xml
<!-- Spring Boot Web -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- H2 Database -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Swagger OpenAPI -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.1.0</version>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

---

## 🧪 Tests

Ejecutar todos los tests:

```bash
C:\Users\Usuario\.maven\maven-3.9.14\bin\mvn test
```

Tests incluidos:
- `CentroAcopioRepositoryTest.java`
- `InventarioRepositoryTest.java`
- `VehiculoRepositoryTest.java`
- `DemoApplicationTests.java`

---

## 📝 API Endpoints

(Ver Swagger UI en http://localhost:8080/swagger-ui.html)

**Operaciones disponibles:**
- GET, POST, PUT, DELETE en Centros de Acopio
- GET, POST, PUT, DELETE en Inventarios
- GET, POST, PUT, DELETE en Vehículos

---

## 🔄 Migración Futura a PostgreSQL

Si en el futuro necesitas migrar a PostgreSQL profesional:

1. Descomentar en `pom.xml`:
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

2. Actualizar `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/microservicios_logistica
spring.datasource.username=logistica
spring.datasource.password=logistica123
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

3. Usar `docker-compose.yml` incluido para levantar PostgreSQL

---

## 📱 Java y Maven

- **Java Version:** 21
- **Spring Boot:** 4.0.5
- **Maven:** 3.9.14 (instalado en `C:\Users\Usuario\.maven\maven-3.9.14\`)

---

## 👨‍💻 Autor

Desarrollado como parte del evaluación del semestre.

---

## 📄 Licencia

Proyecto educativo - Caso Semestral

---

## 📞 Soporte

Para problemas o dudas, revisa:
1. Logs en terminal cuando ejecutes `mvn spring-boot:run`
2. Swagger UI en http://localhost:8080/swagger-ui.html
3. Consola H2 en http://localhost:8080/h2-console
