package org.example.users.app

import org.example.users.domain.models.User
import org.example.users.domain.repository.UserRepository
import org.mindrot.jbcrypt.BCrypt

class RegisterUser(private val userRepository: UserRepository) {

    fun execute(email: String, rawPassword: String, role: String = "PAREJA"): User {

        if (email.isBlank() || !email.contains("@")) {
            throw IllegalArgumentException("El correo no es válido")
        }
        if (rawPassword.length < 6) {
            throw IllegalArgumentException("La contraseña debe tener al menos 6 caracteres")
        }
        if (userRepository.findByEmail(email) != null) {
            throw IllegalArgumentException("Ya existe un usuario con este correo")
        }
        if (role !in listOf("ADMIN", "PAREJA")) {
            throw IllegalArgumentException("Rol inválido")
        }

        val hashedPw = BCrypt.hashpw(rawPassword, BCrypt.gensalt())

        val newUser = User(
            email = email,
            passwordHash = hashedPw,
            role = role
        )

        return userRepository.save(newUser)
    }
}