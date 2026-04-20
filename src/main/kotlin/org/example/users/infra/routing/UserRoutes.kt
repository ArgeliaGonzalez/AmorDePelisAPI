package org.example.users.infra.routing

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.core.utils.saveToTempFile
import org.example.users.app.*
import org.example.users.app.dto.*
import java.io.File
import io.ktor.server.auth.jwt.*

fun Application.userRoutes(
    registerUser: RegisterUser,
    loginUser: LoginUser,
    getUser: GetUser,
    updateUser: UpdateUser,
    deleteUser: DeleteUser
) {
    routing {
        route("/api/v1/users") {

            post("/register") {
                try {
                    val request = call.receive<RegisterUserRequest>()
                    val newUser = registerUser.execute(request.email, request.passwordRaw, request.role, request.username)
                    // (Ajusta registerUser.execute si le pasaste el parámetro username)

                    call.respond(HttpStatusCode.Created, UserResponse(newUser.id, newUser.email, newUser.role, newUser.username, newUser.profileImageUrl))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error")))
                }
            }

            post("/login") {
                try {
                    val request = call.receive<LoginRequest>()
                    val (token, role) = loginUser.execute(request.email, request.passwordRaw)
                    call.respond(HttpStatusCode.OK, TokenResponse(token, role))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Acceso denegado"))
                }
            }

            authenticate("auth-jwt") {

                get("/{id}") {
                    try {
                        val id = call.parameters["id"]?.toInt() ?: throw Exception("ID inválido")
                        val user = getUser.execute(id)
                        call.respond(HttpStatusCode.OK, UserResponse(user.id, user.email, user.role, user.username, user.profileImageUrl))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.NotFound, mapOf("error" to "Usuario no encontrado"))
                    }
                }

                put("/{id}") {
                    var username: String? = null
                    var imageFile: File? = null

                    try {
                        val id = call.parameters["id"]?.toInt() ?: throw Exception("ID inválido")

                        val principal = call.principal<JWTPrincipal>()
                        val idFromToken = principal?.payload?.getClaim("userId")?.asInt()

                        if (idFromToken != id) {
                            call.respond(HttpStatusCode.Forbidden, mapOf("error" to "No tienes permiso para modificar este perfil"))
                            return@put
                        }

                        val multipartData = call.receiveMultipart()

                        multipartData.forEachPart { part ->
                            when (part) {
                                is PartData.FormItem -> {
                                    if (part.name == "username") username = part.value
                                }
                                is PartData.FileItem -> {
                                    if (part.name == "image") imageFile = part.saveToTempFile()
                                }
                                else -> {}
                            }
                            part.dispose()
                        }

                        val updatedUser = updateUser.execute(id, username, imageFile)
                        call.respond(HttpStatusCode.OK, UserResponse(updatedUser.id, updatedUser.email, updatedUser.role, updatedUser.username, updatedUser.profileImageUrl))

                    } catch (e: Exception) {
                        imageFile?.delete()
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al actualizar")))
                    }
                }

                delete("/{id}") {
                    try {
                        val id = call.parameters["id"]?.toInt() ?: throw Exception("ID inválido")

                        val principal = call.principal<JWTPrincipal>()
                        val idFromToken = principal?.payload?.getClaim("userId")?.asInt()

                        if (idFromToken != id) {
                            call.respond(HttpStatusCode.Forbidden, mapOf("error" to "No tienes permiso para eliminar este perfil"))
                            return@delete
                        }

                        deleteUser.execute(id)
                        call.respond(HttpStatusCode.OK, mapOf("message" to "Usuario eliminado"))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al eliminar"))
                    }
                }
            }
        }
    }
}
