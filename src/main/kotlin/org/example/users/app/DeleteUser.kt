package org.example.users.app
import org.example.users.domain.repository.UserRepository

class DeleteUser(private val repository: UserRepository) {
    fun execute(id: Int) {
        repository.delete(id)
    }
}