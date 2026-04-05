package org.example.users.domain.repository

import org.example.users.domain.models.User

interface UserRepository {
    fun findByEmail(email: String): User?
    fun findById(id: Int): User?
    fun save(user: User): User
    fun update(user: User): User
    fun delete(id: Int)
}