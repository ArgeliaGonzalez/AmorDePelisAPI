package org.example.virtual_rooms.infra.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.virtual_rooms.app.AddMovieToRoom
import org.example.virtual_rooms.app.CreateVirtualRoom
import org.example.virtual_rooms.app.GetRoomMovies
import org.example.virtual_rooms.app.JoinVirtualRoom
import org.example.virtual_rooms.app.dto.CreateRoomRequest
import org.example.virtual_rooms.app.dto.JoinRoomRequest
import org.example.virtual_rooms.app.dto.RoomResponse
import org.example.movies.app.dto.MovieResponse
import org.example.virtual_rooms.app.dto.UpdateRoomRequest
import org.example.virtual_rooms.app.GetUserRooms
import org.example.virtual_rooms.app.UpdateVirtualRoom
import org.example.virtual_rooms.app.DeleteVirtualRoom

fun Application.virtualRoomRoutes(
    createVirtualRoom: CreateVirtualRoom,
    joinVirtualRoom: JoinVirtualRoom,
    addMovieToRoom: AddMovieToRoom,
    getRoomMovies: GetRoomMovies,
    getUserRooms: GetUserRooms,
    updateVirtualRoom: UpdateVirtualRoom,
    deleteVirtualRoom: DeleteVirtualRoom
) {
    routing {
        // Todo este bloque está protegido: Requiere Token
        authenticate("auth-jwt") {
            route("/api/v1/rooms") {

                get {
                    try {
                        val principal = call.principal<JWTPrincipal>()
                        val currentUserId = principal?.payload?.getClaim("userId")?.asInt() ?: throw Exception("Token inválido")

                        val rooms = getUserRooms.execute(currentUserId)
                        val response = rooms.map { RoomResponse(it.id, it.roomName, it.invitationCode, it.creatorId, it.guestId) }

                        call.respond(HttpStatusCode.OK, response)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener tus salas"))
                    }
                }

                // PUT: Actualizar nombre de mi sala
                put("/{roomId}") {
                    try {
                        val roomId = call.parameters["roomId"]?.toInt() ?: throw Exception("ID inválido")
                        val request = call.receive<UpdateRoomRequest>()

                        val principal = call.principal<JWTPrincipal>()
                        val currentUserId = principal?.payload?.getClaim("userId")?.asInt() ?: throw Exception("Token inválido")

                        val room = updateVirtualRoom.execute(roomId, currentUserId, request.roomName)
                        call.respond(HttpStatusCode.OK, RoomResponse(room.id, room.roomName, room.invitationCode, room.creatorId, room.guestId))

                    } catch (e: IllegalAccessException) {
                        call.respond(HttpStatusCode.Forbidden, mapOf("error" to e.message))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al actualizar")))
                    }
                }

                // DELETE: Eliminar mi sala
                delete("/{roomId}") {
                    try {
                        val roomId = call.parameters["roomId"]?.toInt() ?: throw Exception("ID inválido")

                        val principal = call.principal<JWTPrincipal>()
                        val currentUserId = principal?.payload?.getClaim("userId")?.asInt() ?: throw Exception("Token inválido")

                        deleteVirtualRoom.execute(roomId, currentUserId)
                        call.respond(HttpStatusCode.OK, mapOf("message" to "Sala eliminada con éxito"))

                    } catch (e: IllegalAccessException) {
                        call.respond(HttpStatusCode.Forbidden, mapOf("error" to e.message))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al eliminar")))
                    }
                }

                post {
                    try {
                        val request = call.receive<CreateRoomRequest>()

                        val principal = call.principal<JWTPrincipal>()
                        val currentUserId = principal?.payload?.getClaim("userId")?.asInt()
                            ?: throw IllegalArgumentException("Token inválido")

                        val room = createVirtualRoom.execute(request.roomName, currentUserId)

                        val response = RoomResponse(room.id, room.roomName, room.invitationCode, room.creatorId, room.guestId)
                        call.respond(HttpStatusCode.Created, response)

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al crear sala")))
                    }
                }

                post("/join") {
                    try {
                        val request = call.receive<JoinRoomRequest>()

                        val principal = call.principal<JWTPrincipal>()
                        val currentUserId = principal?.payload?.getClaim("userId")?.asInt()
                            ?: throw IllegalArgumentException("Token inválido")

                        val room = joinVirtualRoom.execute(request.invitationCode, currentUserId)

                        val response = RoomResponse(room.id, room.roomName, room.invitationCode, room.creatorId, room.guestId)
                        call.respond(HttpStatusCode.OK, response)

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al unirse")))
                    }
                }

                post("/{roomId}/movies/{movieId}") {
                    try {
                        val roomId = call.parameters["roomId"]?.toInt() ?: throw Exception()
                        val movieId = call.parameters["movieId"]?.toInt() ?: throw Exception()

                        addMovieToRoom.execute(roomId, movieId)
                        call.respond(HttpStatusCode.OK, mapOf("message" to "Película añadida a la sala"))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to "No se pudo añadir"))
                    }
                }

                get("/{roomId}/movies") {
                    try {
                        val roomId = call.parameters["roomId"]?.toInt() ?: throw Exception()
                        val movies = getRoomMovies.execute(roomId)

                        val response = movies.map { movie ->
                            MovieResponse(
                                id = movie.id,
                                title = movie.title,
                                imageUrl = movie.imageUrl
                            )
                        }

                        call.respond(HttpStatusCode.OK, response)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener cartelera"))
                    }
                }
            }
        }
    }
}