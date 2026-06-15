# Gestión de Camiones

## Descripción General del Sistema

**Gestión de Camiones** es una API REST desarrollada para empresas dedicadas a la recolección de residuos. El sistema permite administrar camiones, choferes, clientes, rutas, servicios y tareas operativas asociadas a la actividad diaria de recolección.

El objetivo principal es centralizar la gestión de la flota y optimizar la planificación de servicios mediante una arquitectura basada en Spring Boot.

---

## Integrantes del Grupo

* Walter Diaz
* Agustín Christensen
* Arian Shaffer

---

## Tecnologías Utilizadas

### Backend

* Java 17
* Spring Boot 3.5
* Spring Data JPA
* Spring Security
* JWT (JSON Web Token)
* Hibernate

### Base de Datos

* MySQL

### Documentación

* OpenAPI / Swagger UI

### Herramientas

* Maven
* Git
* GitHub
* Jira

---

## Instrucciones para Ejecutar el Proyecto

### Requisitos Previos

* Java 17 o superior
* Maven 3.9 o superior
* MySQL 8 o superior

### Clonar el repositorio

```bash
git clone https://github.com/Arianfer/GestionDeCamiones.git
```

### Configurar la Base de Datos

Crear una base de datos MySQL:

```sql
CREATE DATABASE gestion_camiones;
```

### Configurar application.properties

Completar los siguientes parámetros:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gestion_de_camiones?createDatabaseIfNotExist=true
spring.datasource.username= Usuario
spring.datasource.password= Contraseña

jwt.secret=TU_CLAVE_JWT
jwt.expiration=86400000
```

### Ejecutar la aplicación

```bash
mvn spring-boot:run
```

o

```bash
mvn clean install
java -jar target/proyecto-final.jar
```

---

## Configuración Necesaria

La aplicación requiere:

* Conexión a MySQL.
* Clave secreta JWT configurada.
* Dependencias Maven descargadas correctamente.
* Puerto disponible para Spring Boot (por defecto 8080).

---

## Estructura General del Proyecto

```text
src/main/java
│
├── Controller
├── Service
├── Repository
├── Model
├── DTO
│   ├── Request
│   └── Response
├── config
├── excepcion
└── Enums
```
---

## Listado de Endpoints

### Autenticación

| Método | Endpoint        |
| ------ | --------------- |
| POST   | /api/auth/login |

---

### Usuarios

| Método | Endpoint                |
| ------ | ----------------------- |
| GET    | /api/usuarios           |
| GET    | /api/usuarios/{id}      |
| GET    | /api/usuarios/dni/{dni} |
| POST   | /api/usuarios           |
| PUT    | /api/usuarios/{id}      |
| POST   | /api/usuarios/login     |

---

### Camiones

| Método | Endpoint                             |
| ------ | ------------------------------------ |
| GET    | /api/camiones/listar                 |
| GET    | /api/camiones/obtener/{id}           |
| GET    | /api/camiones/{id}/costo-combustible |
| POST   | /api/camiones/guardar                |
| DELETE | /api/camiones/eliminar/{id}          |

---

### Clientes

| Método | Endpoint           |
| ------ | ------------------ |
| GET    | /api/clientes      |
| GET    | /api/clientes/{id} |
| POST   | /api/clientes      |
| PUT    | /api/clientes/{id} |
| DELETE | /api/clientes/{id} |

---

### Rutas

| Método | Endpoint                   |
| ------ | -------------------------- |
| GET    | /api/rutas                 |
| GET    | /api/rutas/{id}            |
| GET    | /api/rutas/nombre/{nombre} |
| POST   | /api/rutas                 |
| PUT    | /api/rutas/{id}            |
| DELETE | /api/rutas/{id}            |

---

### Servicios

| Método | Endpoint                 |
| ------ | ------------------------ |
| GET    | /api/servicios           |
| GET    | /api/servicios/{id}      |
| POST   | /api/servicios           |
| PUT    | /api/servicios/{id}      |
| PUT    | /api/servicios/reordenar |
| DELETE | /api/servicios/{id}      |

---

### Tareas

| Método | Endpoint                       |
| ------ | ------------------------------ |
| GET    | /api/tareas                    |
| GET    | /api/tareas/{id}               |
| GET    | /api/tareas/mi-ruta/{choferId} |
| POST   | /api/tareas                    |

---

## Ejemplos de Requests y Responses

### Login

Request

```json
{
  "dni": "12345678",
  "password": "1234"
}
```

Response

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

### Crear Cliente

Request

```json
{
  "cuit": "30712345678",
  "razonSocial": "Empresa Ejemplo SA"
}
```

Response

```json
{
  "id": 1,
  "cuit": "30712345678",
  "razonSocial": "Empresa Ejemplo SA"
}
```

---

### Crear Camión

Request

```json
{
  "patente": "ABC123",
  "tipo": "RECOLECTOR",
  "estadoCamion": "DISPONIBLE",
  "capacidadCarga": 12000,
  "consumoDieselPorKm": 0.35
}
```

Response

```json
{
  "id": 1,
  "patente": "ABC123"
}
```

---

## Usuarios de Prueba

En caso de utilizar datos de prueba:

| Usuario | Contraseña | Rol    |
| ------- | ---------- | ------ |
| admin   | admin123   | ADMIN  |
| chofer1 | chofer123  | CHOFER |

---

## Documentación Swagger

La API dispone de documentación interactiva mediante Swagger.

Acceso:

```text
http://localhost:8080/swagger-ui/index.html
```

---

