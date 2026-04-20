package org.example.users.app

import org.example.users.domain.models.User
import org.example.users.domain.repository.UserRepository
import org.mindrot.jbcrypt.BCrypt

class RegisterUser(private val userRepository: UserRepository) {

    fun execute(email: String, rawPassword: String, role: String = "PAREJA", username: String? = null): User {
        if (email.isBlank() || !email.contains("@")) {
            throw IllegalArgumentException("El correo no es valido")
        }
        if (rawPassword.length < 6) {
            throw IllegalArgumentException("La contrasena debe tener al menos 6 caracteres")
        }
        if (userRepository.findByEmail(email) != null) {
            throw IllegalArgumentException("Ya existe un usuario con este correo")
        }
        if (role !in listOf("ADMIN", "PAREJA")) {
            throw IllegalArgumentException("Rol invalido")
        }

        val hashedPw = BCrypt.hashpw(rawPassword, BCrypt.gensalt())

        val newUser = User(
            email = email,
            passwordHash = hashedPw,
            role = role,
            username = username?.takeIf { it.isNotBlank() }
        )

        return userRepository.save(newUser)
    }
}
