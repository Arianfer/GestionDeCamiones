# Gestión de Camiones - Documentación Técnica

## 1. Descripción General del Sistema

**Gestión de Camiones** es una API REST desarrollada para empresas dedicadas a la recolección de residuos. El sistema permite administrar camiones, choferes, clientes, rutas, servicios y tareas operativas asociadas a la actividad diaria de recolección.

El objetivo principal es centralizar la gestión de la flota y optimizar la planificación de servicios mediante una arquitectura basada en Spring Boot.

---

## 2. Integrantes del Grupo

* Walter Diaz
* Agustín Christensen
* Arian Shaffer

---

## 3. Tecnologías Utilizadas

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

## 4. Instrucciones para Ejecutar el Proyecto

### Requisitos Previos

* Java 17 o superior
* Maven 3.9 o superior
* MySQL 8 o superior

### Clonar el repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
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
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD

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

## 5. Configuración Necesaria

La aplicación requiere:

* Conexión a MySQL.
* Clave secreta JWT configurada.
* Dependencias Maven descargadas correctamente.
* Puerto disponible para Spring Boot (por defecto 8080).

---

## 6. Estructura General del Proyecto

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

### Descripción

* Controller: expone los endpoints REST.
* Service: contiene la lógica de negocio.
* Repository: acceso a datos mediante JPA.
* Model: entidades persistentes.
* DTO: objetos de transferencia de datos.
* config: configuración de seguridad y JWT.
* excepcion: manejo centralizado de errores.
* Enums: enumeraciones utilizadas por el sistema.

---

## 7. Descripción de las Entidades Principales

### Usuario

Representa a los usuarios del sistema.

Atributos principales:

* id
* nombre
* apellido
* dni
* email
* password
* activo
* rol

Roles disponibles:

* ADMIN
* CHOFER

---

### Camion

Representa los vehículos de la flota.

Atributos principales:

* id
* patente
* tipo
* estadoCamion
* capacidadCarga
* consumoDieselPorKm

---

### Cliente

Representa las empresas o clientes que solicitan servicios.

Atributos principales:

* id
* cuit
* razonSocial

Relaciones:

* Un cliente puede poseer múltiples servicios.

---

### Ruta

Representa los recorridos de recolección.

Atributos principales:

* idRuta
* nombre
* descripcion
* fecha

Relaciones:

* Un chofer puede estar asignado a una ruta.
* Una ruta puede contener múltiples servicios.

---

### Servicio

Representa un punto de recolección.

Atributos principales:

* id
* nombre
* prioridad
* direccion
* tipoResiduo
* frecuencia
* latitud
* longitud
* orden

Relaciones:

* Pertenece a un cliente.
* Pertenece a una ruta.

---

### Tarea

Representa una actividad operativa asignada.

Atributos principales:

* id
* descripcion
* fechaEjecucion
* estado

Relaciones:

* Asociada a un camión.
* Asociada a una ruta.
* Asociada a un usuario.

---

## 8. Sistema de Autenticación y Autorización

La API utiliza autenticación basada en JWT (JSON Web Token).

### Flujo

1. El usuario envía credenciales.
2. Spring Security valida los datos.
3. Se genera un JWT.
4. El cliente envía el token en cada solicitud.

Cabecera requerida:

```http
Authorization: Bearer <token>
```

Los roles del sistema permiten restringir el acceso a determinados recursos.

---

## 9. Listado de Endpoints

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

## 10. Ejemplos de Requests y Responses

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
  "tipo": "COMPACTADOR",
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

## 11. Usuarios de Prueba

En caso de utilizar datos de prueba:

| Usuario | Contraseña | Rol    |
| ------- | ---------- | ------ |
| admin   | admin123   | ADMIN  |
| chofer1 | chofer123  | CHOFER |

---

## 12. Documentación Swagger

La API dispone de documentación interactiva mediante Swagger.

Acceso:

```text
http://localhost:8080/swagger-ui/index.html
```

---

