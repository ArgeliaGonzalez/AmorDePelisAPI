package org.example.users.app

import org.example.core.security.JwtConfig
import org.example.users.domain.repository.UserRepository
import org.mindrot.jbcrypt.BCrypt

class LoginUser(private val userRepository: UserRepository) {

    fun execute(email: String, rawPassword: String): Pair<String, String> {
        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("Credenciales incorrectas")

        if (!BCrypt.checkpw(rawPassword, user.passwordHash)) {
            throw IllegalArgumentException("Credenciales incorrectas")
        }

        val token = JwtConfig.generateToken(user.id, user.role)

        return Pair(token, user.role)
    }
}