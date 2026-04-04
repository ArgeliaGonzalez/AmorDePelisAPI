package org.example.users.infra

import io.ktor.server.application.*
import org.example.users.app.RegisterUser
import org.example.users.infra.persistence.PostgresUserRepository
import org.example.users.infra.routing.userRoutes

fun Application.initUserModule() {

    val userRepository = PostgresUserRepository()

    val registerUser = RegisterUser(userRepository)

    userRoutes(registerUser)
}