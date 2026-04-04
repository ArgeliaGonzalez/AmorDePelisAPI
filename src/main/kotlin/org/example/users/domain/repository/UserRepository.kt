package org.example.users.domain.repository

import org.example.users.domain.models.User

interface UserRepository {
    fun findByEmail(email: String): User?
    fun save(user: User): User
}