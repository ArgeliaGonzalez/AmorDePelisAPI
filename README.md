# Amor de Pelis — API REST

> Documentación oficial del backend para la aplicación móvil **Amor de Pelis**.  
> Construida con **Kotlin · Ktor · PostgreSQL** e integración con **Cloudinary** para el manejo de imágenes.

---

## Tabla de Contenidos

- [URL Base](#url-base)
- [Autenticación](#autenticación)
- [Módulo de Usuarios](#1-módulo-de-usuarios)
- [Módulo de Salas Virtuales](#2-módulo-de-salas-virtuales)
- [Módulo de Noticias](#3-módulo-de-noticias)
- [Módulo de Productos](#4-módulo-de-productos)

---

## URL Base

| Entorno      | URL                                  |
|--------------|--------------------------------------|
| Producción   | `http://52.7.191.169/api/v1`         |
| Desarrollo   | `http://localhost:8080/api/v1`       |

---

## Autenticación

La mayoría de los endpoints están protegidos mediante **JWT**. Incluye el token en cada petición protegida usando la siguiente cabecera HTTP:

```
Authorization: Bearer <tu_token_jwt>
```

---

## 1. Módulo de Usuarios

**Base:** `/users`

---

### Registrar Usuario

```
POST /users/register
```

**Auth requerida:** No

**Body — `application/json`**
```json
{
  "email": "usuario@correo.com",
  "passwordRaw": "contrasena123",
  "role": "PAREJA",
  "username": "Vlash"
}
```

| Campo         | Tipo   | Requerido | Notas                          |
|---------------|--------|-----------|--------------------------------|
| `email`       | String | Sí        |                                |
| `passwordRaw` | String | Sí        |                                |
| `role`        | String | Sí        | Valores: `ADMIN` o `PAREJA`    |
| `username`    | String | No        | Nombre de visualización        |

---

### Iniciar Sesión

```
POST /users/login
```

**Auth requerida:** No

**Body — `application/json`**
```json
{
  "email": "usuario@correo.com",
  "passwordRaw": "contrasena123"
}
```

**Respuesta exitosa — `200 OK`**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6...",
  "role": "PAREJA"
}
```

---

### Obtener Perfil de Usuario

```
GET /users/{id}
```

**Auth requerida:** Sí

---

### Actualizar Perfil de Usuario

```
PUT /users/{id}
```

**Auth requerida:** Sí — solo el dueño de la cuenta

**Body — `multipart/form-data`**

| Campo      | Tipo   | Descripción                        |
|------------|--------|------------------------------------|
| `username` | Text   | Nuevo nombre de usuario            |
| `image`    | File   | Archivo de imagen (`.png`, `.jpg`) |

---

### Eliminar Usuario

```
DELETE /users/{id}
```

**Auth requerida:** Sí — solo el dueño de la cuenta

---

## 2. Módulo de Salas Virtuales

**Base:** `/rooms`

---

### Ver mis Salas

```
GET /rooms
```

**Auth requerida:** Sí

Devuelve una lista de las salas donde el usuario autenticado es **creador** o **invitado**.

---

### Crear Sala

```
POST /rooms
```

**Auth requerida:** Sí

**Body — `application/json`**
```json
{
  "roomName": "Noche de Terror"
}
```

---

### Unirse a una Sala

```
POST /rooms/join
```

**Auth requerida:** Sí

**Body — `application/json`**
```json
{
  "invitationCode": "A1B2C3"
}
```

---

### Actualizar Nombre de Sala

```
PUT /rooms/{roomId}
```

**Auth requerida:** Sí — solo el creador de la sala

**Body — `application/json`**
```json
{
  "roomName": "Nuevo Nombre de Sala"
}
```

---

### Eliminar Sala

```
DELETE /rooms/{roomId}
```

**Auth requerida:** Sí — solo el creador de la sala

---

### Añadir Película a la Sala

```
POST /rooms/{roomId}/movies/{movieId}
```

**Auth requerida:** Sí

---

### Ver Cartelera de la Sala

```
GET /rooms/{roomId}/movies
```

**Auth requerida:** Sí

---

## 3. Módulo de Noticias

**Base:** `/news`

---

### Obtener Todas las Noticias

```
GET /news
```

**Auth requerida:** Sí

---

### Obtener Última Noticia

```
GET /news/latest
```

**Auth requerida:** Sí

Devuelve un único objeto JSON con la noticia más reciente.

---

### Crear Noticia

```
POST /news
```

**Auth requerida:** Sí — solo rol `ADMIN`

**Body — `multipart/form-data`**

| Campo     | Tipo | Descripción                    |
|-----------|------|--------------------------------|
| `title`   | Text | Título de la noticia           |
| `content` | Text | Cuerpo de la noticia           |
| `image`   | File | Archivo de imagen adjunto      |

---

## 4. Módulo de Productos

**Base:** `/products`

---

### Obtener Productos

```
GET /products
```

**Auth requerida:** No

---

### Crear Producto

```
POST /products
```

**Auth requerida:** Sí — solo rol `ADMIN`

**Body — `multipart/form-data`**

| Campo         | Tipo | Descripción                              |
|---------------|------|------------------------------------------|
| `name`        | Text | Nombre del producto (ej. Kit Maratón Premium) |
| `description` | Text | Descripción del producto                 |
| `image`       | File | Archivo de imagen adjunto                |

---

<div align="center">

**Amor de Pelis** · Backend · v1.0

</div>