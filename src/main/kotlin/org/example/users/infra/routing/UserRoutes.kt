package org.example.users.infra.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.users.app.RegisterUser
import org.example.users.app.dto.RegisterUserRequest
import org.example.users.app.dto.UserResponse

fun Application.userRoutes(registerUser: RegisterUser) {
    routing {
        route("/api/v1/users") {

            post("/register") {
                try {
                    // 1. Recibir el JSON y convertirlo automáticamente al DTO
                    val request = call.receive<RegisterUserRequest>()

                    // 2. Ejecutar el caso de uso (nuestro núcleo hexagonal)
                    val newUser = registerUser.execute(
                        email = request.email,
                        rawPassword = request.passwordRaw,
                        role = request.role
                    )

                    // 3. Mapear el modelo de dominio al DTO de respuesta
                    val response = UserResponse(
                        id = newUser.id,
                        email = newUser.email,
                        role = newUser.role
                    )

                    // 4. Responder con HTTP 201 (Created) y el usuario creado
                    call.respond(HttpStatusCode.Created, response)

                } catch (e: IllegalArgumentException) {
                    // Si falla alguna validación del dominio, devolvemos un 400 Bad Request
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Datos inválidos")))
                } catch (e: Exception) {
                    // Si ocurre un error inesperado (ej. se cae la base de datos)
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error interno del servidor"))
                }
            }

        }
    }
}