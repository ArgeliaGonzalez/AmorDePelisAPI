package org.example.users.domain.models

data class User(
    val id: Int = 0,
    val email: String,
    val passwordHash: String,
    val role: String
)