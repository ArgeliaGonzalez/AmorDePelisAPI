package org.example.users.app.dto

import kotlinx.serialization.Serializable

// Lo que esperamos recibir del cliente (App móvil/Postman)
@Serializable
data class RegisterUserRequest(
    val email: String,
    val passwordRaw: String,
    val role: String = "PAREJA"
)

// Lo que le devolvemos al cliente (Sin datos sensibles)
@Serializable
data class UserResponse(
    val id: Int,
    val email: String,
    val role: String
)
// Lo que nos envía el cliente para iniciar sesión
@Serializable
data class LoginRequest(
    val email: String,
    val passwordRaw: String
)

// Lo que le devolvemos si el login es exitoso
@Serializable
data class TokenResponse(
    val token: String,
    val role: String
)

