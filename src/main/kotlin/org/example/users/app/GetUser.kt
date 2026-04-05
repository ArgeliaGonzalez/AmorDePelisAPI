package org.example.users.app
import org.example.users.domain.models.User
import org.example.users.domain.repository.UserRepository

class GetUser(private val repository: UserRepository) {
    fun execute(id: Int): User {
        return repository.findById(id) ?: throw Exception("Usuario no encontrado")
    }
}