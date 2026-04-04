package org.example.core.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.example.core.database.DatabaseFactory
import java.util.Date

object JwtConfig {
    private val secret = DatabaseFactory.getEnv("JWT_SECRET")
    private val issuer = DatabaseFactory.getEnv("JWT_ISSUER")
    private val algorithm = Algorithm.HMAC256(secret)

    fun generateToken(userId: Int, role: String): String {
        return JWT.create()
            .withIssuer(issuer)
            .withClaim("userId", userId)
            .withClaim("role", role)
            .withExpiresAt(Date(System.currentTimeMillis() + 86400000))
            .sign(algorithm)
    }

    val verifier = JWT.require(algorithm).withIssuer(issuer).build()
    const val myRealm = "AmorDePelisRealm"
}