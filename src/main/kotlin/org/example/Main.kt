package org.example

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import org.example.core.database.DatabaseFactory
import org.example.core.security.JwtConfig
import org.example.users.infra.initUserModule
import org.example.virtual_rooms.infra.initVirtualRoomModule
import org.example.movies.infra.initMoviesModule
import org.example.history.infra.initHistoryModule
import org.example.custom_lists.infra.initCustomListsModule

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    // Configuración global de Autenticación JWT
    install(Authentication) {
        jwt("auth-jwt") {
            realm = JwtConfig.myRealm
            verifier(JwtConfig.verifier)
            validate { credential ->
                if (credential.payload.getClaim("userId").asInt() != null) {
                    JWTPrincipal(credential.payload)
                } else null
            }
        }
    }

    DatabaseFactory.init()

    initUserModule()
    initVirtualRoomModule()
    initMoviesModule()
    initHistoryModule()
    initCustomListsModule()
}