# Microservicios EVA1 - Gestion de Logistica

Repositorio academico con microservicio de gestion de logistica y su frontend.

## Estructura

- demo/: backend Spring Boot
- frontend/: frontend React + Vite
- docs/: documentacion del proyecto

## Documentacion

- docs/README.md: indice de documentos
- docs/IEEE_830_Especificacion_Requisitos.md
- docs/PATRONES_DE_DISEÑO.md
- docs/PRESENTACION.md
- docs/REPORTE_IMPLEMENTACION.md

## Requisitos

- Java 21
- Maven Wrapper (mvnw)
- Node.js 18+

## Ejecucion (desarrollo)

Backend:

```
cd demo
.\mvnw spring-boot:run
```

Frontend:

```
cd frontend
npm install
npm run dev
```

API base: http://localhost:8080/api/logistica

## Notas

La documentacion formal del proyecto esta en la carpeta docs/.
