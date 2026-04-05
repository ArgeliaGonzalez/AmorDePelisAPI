package org.example.users.app.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserRequest(
    val email: String,
    val passwordRaw: String,
    val role: String,
    val username: String? = null
)

@Serializable
data class LoginRequest(
    val email: String,
    val passwordRaw: String
)

@Serializable
data class TokenResponse(
    val token: String,
    val role: String
)

@Serializable
data class UserResponse(
    val id: Int,
    val email: String,
    val role: String,
    val username: String?,
    val profileImageUrl: String?
)