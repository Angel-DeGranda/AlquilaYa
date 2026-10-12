# AlquilaYa API

API REST para la gestión de una empresa de alquiler de vehículos ("AlquilaYa S.A."). Forma parte del reto de Arquitectura Orientada a Servicios (SOA) y permite que aplicaciones móviles y sitios web de terceros consulten el inventario de vehículos y gestionen alquileres y mantenimientos.

## Descripción de la API

La API expone la gestión de:

- **Oficinas de alquiler**: ubicaciones físicas donde se administran los vehículos.
- **Trabajadores**: personal asignado a una oficina.
- **Vehículos**: flota de la empresa, con cuatro tipos (coche, moto, furgoneta y coche eléctrico) y una regla de precio distinta para cada uno.
- **Clientes**: personas que alquilan vehículos.
- **Alquileres**: contrato entre un cliente y un vehículo en un periodo. El precio total se calcula automáticamente.
- **Mantenimientos**: historial de reparaciones y revisiones de cada vehículo.

### Modelo de datos

Cada entidad tiene una clave primaria llamada `id` seguido del nombre de la entidad.

| Entidad | Clave primaria | Relaciones |
|---|---|---|
| Oficina de alquiler | `idOficina` | Tiene muchos trabajadores y muchos vehículos |
| Trabajador | `idTrabajador` | Pertenece a una oficina |
| Vehículo | `idVehiculo` | Pertenece a una oficina |
| Cliente | `idCliente` | Tiene muchos alquileres |
| Alquiler | `idAlquiler` | Relaciona un vehículo con un cliente |
| Mantenimiento | `idMantenimiento` | Relaciona un vehículo, un trabajador responsable y una oficina responsable |

### Herencia de vehículos

`Vehiculo` es una clase abstracta y se guarda en una sola tabla (estrategia `SINGLE_TABLE` de JPA) con una columna discriminadora `tipo_vehiculo`. Cada subtipo calcula su precio de alquiler con su propia regla (polimorfismo):

| Tipo | Valor de `tipo` | Regla de precio para `n` días |
|---|---|---|
| Coche | `COCHE` | tarifa x n; 10% de descuento a partir de 7 días |
| Moto | `MOTO` | (tarifa + 3) x n; más 5 por día si la cilindrada es mayor a 500 cc |
| Furgoneta | `FURGONETA` | tarifa x n; más 15 por día si la capacidad de carga es mayor a 1000 kg |
| Coche eléctrico | `COCHE_ELECTRICO` | precio de un coche con 15% de descuento ecológico |

### Reglas de negocio principales

- El precio total de un alquiler lo calcula el servidor; el cliente de la API no lo envía.
- Un vehículo solo se puede alquilar si está `Disponible`, y pasa a `Alquilado`. Al completar el alquiler vuelve a `Disponible`.
- Solo un cliente con estatus `Activo` puede alquilar.
- La fecha de fin de un alquiler debe ser posterior a la de inicio.
- Al registrar un mantenimiento, el vehículo pasa a `Mantenimiento`; al cerrarlo vuelve a `Disponible`. Solo un vehículo `Disponible` puede enviarse a mantenimiento.
- Si un mantenimiento se crea sin `idOficinaResponsable`, se usa la oficina del vehículo.
- El correo y la licencia de conducir de clientes y trabajadores son únicos, y la matrícula de los vehículos también.
- Las contraseñas se almacenan encriptadas (BCrypt) y nunca se devuelven en las respuestas.

## Tecnologías utilizadas

- Java 21
- Spring Boot (Spring Web, Spring Data JPA, Spring Security, Bean Validation)
- Hibernate / JPA
- MySQL y su controlador JDBC
- Maven
- BCrypt para el cifrado de contraseñas

### Arquitectura

El proyecto sigue una arquitectura en capas:

```
com.api.alquilaya
├── config         Seguridad (SecurityConfig) y carga inicial de usuarios
├── controllers    Controladores REST (@RestController)
├── dto            Objetos de entrada (AlquilerRequest, MantenimientoRequest)
├── exception      Excepciones propias y manejador global de errores
├── models         Entidades JPA (@Entity)
├── repositories   Interfaces que extienden JpaRepository
└── services       Lógica de negocio (@Service)
```

## Pasos de instalación y ejecución

### Requisitos previos

- JDK 21
- MySQL 8 o superior en ejecución
- Maven (opcional: el proyecto incluye el wrapper `mvnw`)

### Instalación

1. Clona el repositorio y entra a la carpeta del proyecto:

   ```bash
   git clone <url-del-repositorio>
   cd alquilaya
   ```

2. Crea la base de datos en MySQL:

   ```sql
   CREATE DATABASE alquilaya;
   ```

   Las tablas se crean solas al iniciar la aplicación (`spring.jpa.hibernate.ddl-auto=update`).

3. Define las variables de entorno de la siguiente sección.

### Ejecución

En Windows:

```bash
mvnw.cmd spring-boot:run
```

En Linux o macOS:

```bash
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

Al iniciar, la aplicación crea automáticamente un usuario con rol `ADMIN` y otro con rol `OPERADOR` si todavía no existen, usando las credenciales configuradas.

### Verificación rápida

Con las credenciales del administrador (HTTP Basic), esta petición debe responder `200` con el texto `Acceso autorizado`:

```bash
curl -u <ADMIN_EMAIL>:<ADMIN_PASSWORD> http://localhost:8080/api/admin/prueba
```

## Variables de entorno necesarias

| Variable | Descripción | Ejemplo |
|---|---|---|
| `DB_URL` | URL de conexión a MySQL | `jdbc:mysql://localhost:3306/alquilaya` |
| `DB_USERNAME` | Usuario de MySQL | `root` |
| `DB_PASSWORD` | Contraseña de MySQL | `<tu-contraseña>` |
| `ADMIN_EMAIL` | Correo del usuario administrador inicial | `admin@alquilaya.com` |
| `ADMIN_PASSWORD` | Contraseña del administrador inicial | `<contraseña-segura>` |
| `OPERADOR_EMAIL` | Correo del usuario operador inicial | `operador@alquilaya.com` |
| `OPERADOR_PASSWORD` | Contraseña del operador inicial | `<contraseña-segura>` |

El archivo `src/main/resources/application.properties` las lee así:

```properties
spring.application.name=alquilaya

server.port=8080

spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/alquilaya}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false

app.admin.email=${ADMIN_EMAIL:admin@alquilaya.com}
app.admin.password=${ADMIN_PASSWORD}

app.operador.email=${OPERADOR_EMAIL:operador@alquilaya.com}
app.operador.password=${OPERADOR_PASSWORD}
```

Las contraseñas no tienen valor por defecto a propósito, para no publicarlas en el repositorio. Ejemplo de configuración en Windows (PowerShell) antes de ejecutar:

```powershell
$env:DB_PASSWORD="<tu-contraseña>"
$env:ADMIN_PASSWORD="<contraseña-segura>"
$env:OPERADOR_PASSWORD="<contraseña-segura>"
```

## Autenticación y roles

La API usa autenticación HTTP Basic: cada petición debe incluir el header `Authorization` con el correo y la contraseña de un usuario. En Postman: pestaña Authorization, tipo Basic Auth.

| Ruta | Roles con acceso |
|---|---|
| `/api/admin/**` | `ADMIN` |
| `/api/**` (el resto) | `ADMIN` y `OPERADOR` |

Una petición sin credenciales válidas responde `401`, y una con un rol sin permiso responde `403`.

## Listado de endpoints

### Oficinas: `/api/oficinas`

| Método | Ruta | Descripción | Respuesta |
|---|---|---|---|
| GET | `/api/oficinas` | Lista todas las oficinas | 200 |
| GET | `/api/oficinas/{id}` | Obtiene una oficina | 200 / 404 |
| POST | `/api/oficinas` | Crea una oficina | 201 / 400 |
| PUT | `/api/oficinas/{id}` | Actualiza una oficina | 200 / 400 / 404 |
| DELETE | `/api/oficinas/{id}` | Elimina una oficina | 204 / 404 / 409 |

### Trabajadores: `/api/trabajadores`

| Método | Ruta | Descripción | Respuesta |
|---|---|---|---|
| GET | `/api/trabajadores` | Lista todos los trabajadores | 200 |
| GET | `/api/trabajadores/{id}` | Obtiene un trabajador | 200 / 404 |
| POST | `/api/trabajadores` | Crea un trabajador | 201 / 400 / 404 / 409 |
| PUT | `/api/trabajadores/{id}` | Actualiza un trabajador | 200 / 400 / 404 / 409 |
| DELETE | `/api/trabajadores/{id}` | Elimina un trabajador | 204 / 404 / 409 |

### Vehículos: `/api/vehiculos`

| Método | Ruta | Descripción | Respuesta |
|---|---|---|---|
| GET | `/api/vehiculos` | Lista todos los vehículos | 200 |
| GET | `/api/vehiculos/disponibles` | Lista los vehículos con estatus `Disponible` | 200 |
| GET | `/api/vehiculos/{id}` | Obtiene un vehículo | 200 / 404 |
| POST | `/api/vehiculos` | Crea un vehículo (requiere el campo `tipo`) | 201 / 400 / 404 / 409 |
| PUT | `/api/vehiculos/{id}` | Actualiza un vehículo (no se puede cambiar el tipo) | 200 / 400 / 404 / 409 |
| DELETE | `/api/vehiculos/{id}` | Elimina un vehículo | 204 / 404 / 409 |

### Clientes: `/api/clientes`

| Método | Ruta | Descripción | Respuesta |
|---|---|---|---|
| GET | `/api/clientes` | Lista todos los clientes | 200 |
| GET | `/api/clientes/{id}` | Obtiene un cliente | 200 / 404 |
| POST | `/api/clientes` | Crea un cliente | 201 / 400 / 409 |
| PUT | `/api/clientes/{id}` | Actualiza un cliente | 200 / 400 / 404 / 409 |
| DELETE | `/api/clientes/{id}` | Elimina un cliente | 204 / 404 / 409 |

### Alquileres: `/api/alquileres`

| Método | Ruta | Descripción | Respuesta |
|---|---|---|---|
| GET | `/api/alquileres` | Lista todos los alquileres | 200 |
| GET | `/api/alquileres/{id}` | Obtiene un alquiler | 200 / 404 |
| POST | `/api/alquileres` | Crea un alquiler y calcula el precio total | 201 / 400 / 404 |
| PUT | `/api/alquileres/{id}/completar` | Completa el alquiler y libera el vehículo | 200 / 400 / 404 |

### Mantenimientos: `/api/mantenimientos`

| Método | Ruta | Descripción | Respuesta |
|---|---|---|---|
| GET | `/api/mantenimientos/vehiculo/{idVehiculo}` | Historial de mantenimientos de un vehículo | 200 |
| POST | `/api/mantenimientos` | Registra un mantenimiento y pone el vehículo en `Mantenimiento` | 201 / 400 / 404 |
| PUT | `/api/mantenimientos/{id}/cerrar?fechaFin=AAAA-MM-DD` | Cierra el mantenimiento y libera el vehículo | 200 / 400 / 404 |

### Rutas de prueba de acceso

| Método | Ruta | Rol |
|---|---|---|
| GET | `/api/admin/prueba` | `ADMIN` |
| GET | `/api/operador/prueba` | `ADMIN` y `OPERADOR` |

## Ejemplos de peticiones y respuestas

En todos los ejemplos se envía el header `Authorization` (Basic Auth) y, en los POST y PUT, `Content-Type: application/json`.

### Crear una oficina

`POST /api/oficinas`

```json
{
  "nombreOficina": "Oficina Centro",
  "direccion": "Av. Independencia 123",
  "ciudad": "Veracruz",
  "region": "Veracruz"
}
```

Respuesta `201 Created` (incluye el header `Location: http://localhost:8080/api/oficinas/1`):

```json
{
  "idOficina": 1,
  "nombreOficina": "Oficina Centro",
  "direccion": "Av. Independencia 123",
  "ciudad": "Veracruz",
  "region": "Veracruz"
}
```

### Crear un trabajador

`POST /api/trabajadores`

```json
{
  "oficina": { "idOficina": 1 },
  "nombre": "Carlos",
  "apellidos": "Ramírez López",
  "email": "carlos@alquilaya.com",
  "telefono": "2291234567",
  "fechaNacimiento": "1990-05-12",
  "licenciaConducir": "TRB-001",
  "estatus": "Activo",
  "password": "clave1234"
}
```

Respuesta `201 Created` (la contraseña nunca se devuelve):

```json
{
  "idTrabajador": 1,
  "oficina": {
    "idOficina": 1,
    "nombreOficina": "Oficina Centro",
    "direccion": "Av. Independencia 123",
    "ciudad": "Veracruz",
    "region": "Veracruz"
  },
  "nombre": "Carlos",
  "apellidos": "Ramírez López",
  "email": "carlos@alquilaya.com",
  "telefono": "2291234567",
  "fechaNacimiento": "1990-05-12",
  "licenciaConducir": "TRB-001",
  "estatus": "Activo"
}
```

### Crear un vehículo

El campo `tipo` indica qué subclase crear. Los atributos propios de cada tipo son: `cilindrada` (`MOTO`), `capacidadCargaKg` (`FURGONETA`), y `nivelBateria` y `autonomiaMaximaKm` (`COCHE_ELECTRICO`). El estatus inicial es `Disponible` y la fecha de registro se asigna sola.

`POST /api/vehiculos`

```json
{
  "tipo": "MOTO",
  "oficina": { "idOficina": 1 },
  "marca": "Honda",
  "modelo": "CB650R",
  "anio": 2022,
  "matricula": "VER-2002",
  "tarifaDiaria": 300.00,
  "cilindrada": 650
}
```

Respuesta `201 Created`:

```json
{
  "tipo": "MOTO",
  "idVehiculo": 2,
  "oficina": {
    "idOficina": 1,
    "nombreOficina": "Oficina Centro",
    "direccion": "Av. Independencia 123",
    "ciudad": "Veracruz",
    "region": "Veracruz"
  },
  "marca": "Honda",
  "modelo": "CB650R",
  "anio": 2022,
  "matricula": "VER-2002",
  "tarifaDiaria": 300.00,
  "estatus": "Disponible",
  "fechaRegistro": "2026-10-11",
  "cilindrada": 650
}
```

### Crear un cliente

`POST /api/clientes`

```json
{
  "nombre": "Ana",
  "apellidos": "Torres Gómez",
  "email": "ana@correo.com",
  "telefono": "2299876543",
  "fechaNacimiento": "1995-03-20",
  "licenciaConducir": "LIC-0001",
  "estatus": "Activo",
  "password": "clave1234"
}
```

Respuesta `201 Created`:

```json
{
  "idCliente": 1,
  "nombre": "Ana",
  "apellidos": "Torres Gómez",
  "email": "ana@correo.com",
  "telefono": "2299876543",
  "fechaNacimiento": "1995-03-20",
  "licenciaConducir": "LIC-0001",
  "estatus": "Activo"
}
```

### Crear un alquiler

Solo se envían los ids y las fechas. El precio y el estatus los calcula el servidor. Para un coche con tarifa diaria de 500 y 7 días, se aplica el 10% de descuento: 500 x 7 x 0.9 = 3150.

`POST /api/alquileres`

```json
{
  "idVehiculo": 1,
  "idCliente": 1,
  "fechaInicio": "2026-10-12",
  "fechaFin": "2026-10-19"
}
```

Respuesta `201 Created` (los objetos `vehiculo` y `cliente` se devuelven completos; aquí se abrevian):

```json
{
  "idAlquiler": 1,
  "vehiculo": { "tipo": "COCHE", "idVehiculo": 1, "marca": "Nissan", "modelo": "Versa", "estatus": "Alquilado" },
  "cliente": { "idCliente": 1, "nombre": "Ana", "apellidos": "Torres Gómez" },
  "fechaInicioAlquiler": "2026-10-12",
  "fechaFinAlquiler": "2026-10-19",
  "estatus": "Pendiente",
  "precioTotal": 3150.00
}
```

### Completar un alquiler

`PUT /api/alquileres/1/completar` (sin cuerpo)

Respuesta `200 OK`: el mismo alquiler con `"estatus": "Completado"`. El vehículo vuelve a `Disponible`.

### Registrar un mantenimiento

Si se omite `idOficinaResponsable`, se usa la oficina del vehículo. La fecha de fin queda en `null` hasta que se cierra el mantenimiento.

`POST /api/mantenimientos`

```json
{
  "idVehiculo": 1,
  "idTrabajadorResponsable": 1,
  "fechaInicioMantenimiento": "2026-10-20",
  "descripcion": "Cambio de aceite y filtros",
  "costo": 850.00,
  "kilometraje": 45000
}
```

Respuesta `201 Created` (los objetos anidados se abrevian):

```json
{
  "idMantenimiento": 1,
  "vehiculo": { "tipo": "COCHE", "idVehiculo": 1, "estatus": "Mantenimiento" },
  "trabajadorResponsable": { "idTrabajador": 1, "nombre": "Carlos" },
  "oficinaResponsable": { "idOficina": 1, "nombreOficina": "Oficina Centro" },
  "fechaInicioMantenimiento": "2026-10-20",
  "fechaFinMantenimiento": null,
  "descripcion": "Cambio de aceite y filtros",
  "costo": 850.00,
  "kilometraje": 45000
}
```

### Cerrar un mantenimiento

`PUT /api/mantenimientos/1/cerrar?fechaFin=2026-10-22` (sin cuerpo)

Respuesta `200 OK`: el mismo mantenimiento con `"fechaFinMantenimiento": "2026-10-22"`. El vehículo vuelve a `Disponible`.

## Manejo de errores

Todos los errores tienen la misma estructura:

```json
{
  "timestamp": "2026-10-11T18:30:00",
  "status": 404,
  "error": "Not Found",
  "mensaje": "Cliente no encontrado con id 999"
}
```

| Código | Cuándo ocurre | Ejemplo de mensaje |
|---|---|---|
| 400 Bad Request | Datos inválidos, JSON mal formado o regla de negocio incumplida | `email: debe ser una dirección de correo electrónico con formato correcto` |
| 401 Unauthorized | Faltan credenciales o son incorrectas | |
| 403 Forbidden | El rol del usuario no tiene permiso | |
| 404 Not Found | El recurso no existe | `Vehículo no encontrado con id 999` |
| 409 Conflict | Valor duplicado o registro con datos relacionados | `Ya existe un cliente con ese email` |

Ejemplos de errores de negocio:

| Petición | Código | Mensaje |
|---|---|---|
| Crear un alquiler con un vehículo ya alquilado | 400 | `El vehículo no está disponible` |
| Crear un alquiler con `fechaFin` anterior a `fechaInicio` | 400 | `La fecha de fin debe ser posterior a la fecha de inicio` |
| Crear un alquiler con un cliente suspendido | 400 | `El cliente no está activo` |
| Completar un alquiler ya completado | 400 | `El alquiler ya fue completado` |
| Eliminar una oficina que tiene vehículos | 409 | `La operación viola una restricción de datos (registro relacionado o valor duplicado)` |

### Valores permitidos

| Campo | Valores |
|---|---|
| `estatus` de cliente y trabajador | `Activo`, `Inactivo`, `Suspendido` |
| `estatus` de vehículo | `Disponible`, `Alquilado`, `Mantenimiento` |
| `estatus` de alquiler | `Pendiente`, `Activo`, `Completado` |
| `tipo` de vehículo | `COCHE`, `MOTO`, `FURGONETA`, `COCHE_ELECTRICO` |
