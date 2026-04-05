package org.example.users.infra

import io.ktor.server.application.*
import org.example.users.app.*
import org.example.users.infra.persistence.PostgresUserRepository
import org.example.users.infra.routing.userRoutes

fun Application.initUserModule() {
    val userRepository = PostgresUserRepository()

    val registerUser = RegisterUser(userRepository)
    val loginUser = LoginUser(userRepository)
    val getUser = GetUser(userRepository)
    val updateUser = UpdateUser(userRepository)
    val deleteUser = DeleteUser(userRepository)

    userRoutes(registerUser, loginUser, getUser, updateUser, deleteUser)
}