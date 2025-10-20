# 🧩 Pipeline de Integración Continua – SCLOUD.VIDEOCLUB

![CI/CD Status](https://github.com/JavierLobo/SCLOUD.VIDEOCLUB/actions/workflows/build.yml/badge.svg)

Este repositorio implementa un **entorno de integración continua (CI/CD)** completo para todos los microservicios del proyecto **SCLOUD.VIDEOCLUB**, basado en **Spring Boot**, **Spring Cloud**, **Docker** y **SonarQube**.

El workflow se ejecuta automáticamente en GitHub Actions cuando se realizan cambios en las ramas principales:
- `dev` → Desarrollo
- `int` → Integración
- `master` → Producción

---

## ⚙️ Descripción general

El workflow `build.yml` ejecuta las siguientes fases de manera secuencial:

1. **Análisis de calidad con SonarQube**
2. **Verificación del Quality Gate**
3. **Compilación de todos los microservicios con Maven**
4. **Construcción y publicación de imágenes Docker versionadas**
5. **Informe final de estado por servicio**

---

## 🧮 Servicios incluidos

El pipeline analiza, compila y publica imágenes Docker para todos los servicios:

| Microservicio | Descripción |
|----------------|-------------|
| `auth-service` | Autenticación y gestión de usuarios. |
| `config-service` | Configuración centralizada (Spring Cloud Config). |
| `discovery-service` | Registro de servicios (Eureka). |
| `gateway-service` | API Gateway, punto de entrada común. |
| `catalog-service` | Catálogo de películas y metadatos. |
| `customer-service` | Gestión de clientes y perfiles. |
| `inventory-service` | Control de stock y disponibilidad. |
| `notification-service` | Envío de notificaciones y correos. |
| `rental-service` | Procesamiento de alquileres y devoluciones. |

---

## 🔍 Fase 1 – Análisis de código (SonarQube)

Cada microservicio se analiza individualmente en **SonarQube**, verificando:

- Duplicaciones de código
- Complejidad ciclomática
- Cobertura de pruebas
- Code smells
- Vulnerabilidades

Si algún servicio no supera las métricas mínimas, el pipeline **detiene la ejecución antes de compilar** (Quality Gate).

### Requisitos
El servidor de SonarQube debe estar definido en los secretos del repositorio:

| Variable | Descripción |
|-----------|--------------|
| `SONAR_HOST_URL` | URL del servidor SonarQube local o remoto |
| `SONAR_TOKEN` | Token de acceso generado en SonarQube |

---

## ⚙️ Fase 2 – Compilación Maven

Cada servicio se compila de manera independiente mediante:

```bash
mvn clean package -DskipTests
```

La versión utilizada para las imágenes Docker se obtiene **directamente del `pom.xml`** de cada microservicio, garantizando un versionado real y coherente.

---

## 🐳 Fase 3 – Construcción y publicación Docker

Por cada microservicio se generan y publican **tres etiquetas** de imagen en Docker Hub:

| Etiqueta | Descripción |
|-----------|--------------|
| `latest` | Última build válida. |
| `<branch>-<version>` | Identifica la rama y versión Maven (ej. `int-1.3.0`). |
| `<version>` | Coincide con la versión del `pom.xml` (ej. `1.3.0`). |

Ejemplo:
```
javierlobo/catalog-service:1.3.0
javierlobo/catalog-service:int-1.3.0
javierlobo/catalog-service:latest
```

### Autenticación
Las credenciales de DockerHub se definen en los secretos del repositorio:

| Variable | Descripción |
|-----------|--------------|
| `DOCKERHUB_USER` | Usuario de Docker Hub |
| `DOCKERHUB_TOKEN` | Token o contraseña para autenticación |

---

## 🧩 Fase 4 – Ejecución independiente por servicio

Cada microservicio se ejecuta de forma **independiente** dentro del pipeline.  
Si un servicio falla (por SonarQube, build o push Docker), **los demás continúan**, pero se registra el fallo en un reporte final de ejecución.

Esto garantiza que los errores de un módulo no bloqueen todo el proceso.

---

## 📋 Reporte final

Al finalizar el workflow, se genera un resumen con todos los resultados:

```
──────────────────────────────────────────────
📋 SUMMARY REPORT
──────────────────────────────────────────────
✅ Successful services: auth-service catalog-service ...
❌ Failed services: notification-service(Build) rental-service(Sonar)
```

(Opcionalmente puede almacenarse como artefacto descargable en futuras versiones del pipeline.)

---

## 🧠 Recomendaciones

- Mantener actualizados los valores comunes de versiones en el `pom.xml` padre (multi-module).
- Ejecutar el pipeline solo en ramas `dev`, `int` y `master`.
- Utilizar `docker-compose` o `k8s/` para entornos locales y despliegues automatizados.
- Supervisar los resultados de SonarQube periódicamente para detectar regresiones de calidad.

---

## 🏗️ Próximas mejoras planificadas

- [ ] Publicar el reporte de ejecución como artefacto descargable.
- [ ] Integrar despliegue automático en Kubernetes.
- [ ] Añadir cacheo Maven y Docker para acelerar builds.
- [ ] Incluir métricas de cobertura con JaCoCo y Sonar.

---

**Autor:**  
🧑‍💻 *Francisco Javier Lobo Martos*  
📦 Proyecto: `SCLOUD.VIDEOCLUB`  
📅 Última actualización: Octubre 2025
