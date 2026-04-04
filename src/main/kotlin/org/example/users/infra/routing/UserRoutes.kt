package org.example.users.infra.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.users.app.LoginUser
import org.example.users.app.RegisterUser
import org.example.users.app.dto.RegisterUserRequest
import org.example.users.app.dto.UserResponse
import org.example.users.app.dto.TokenResponse
import org.example.users.app.dto.LoginRequest


fun Application.userRoutes(registerUser: RegisterUser, loginUser: LoginUser) {
    routing {
        route("/api/v1/users") {

            post("/register") {
                try {
                    val request = call.receive<RegisterUserRequest>()

                    val newUser = registerUser.execute(
                        email = request.email,
                        rawPassword = request.passwordRaw,
                        role = request.role
                    )

                    val response = UserResponse(
                        id = newUser.id,
                        email = newUser.email,
                        role = newUser.role
                    )

                    call.respond(HttpStatusCode.Created, response)

                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Datos inválidos")))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error interno del servidor"))
                }
            }
            post("/login") {
                try {
                    val request = call.receive<LoginRequest>()

                    val (token, role) = loginUser.execute(request.email, request.passwordRaw)

                    call.respond(HttpStatusCode.OK, TokenResponse(token, role))

                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to (e.message ?: "Acceso denegado")))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error interno del servidor"))
                }
            }

        }
    }
}