package org.example.virtual_rooms.infra.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.favorites.domain.repository.FavoriteRepository
import org.example.history.domain.repository.HistoryRepository
import org.example.movies.app.dto.MovieResponse
import org.example.movies.domain.models.Movie
import org.example.tags.app.GetMovieTags
import org.example.tags.app.dto.TagResponse
import org.example.users.domain.models.User
import org.example.users.domain.repository.UserRepository
import org.example.virtual_rooms.app.AddMovieToRoom
import org.example.virtual_rooms.app.CreateVirtualRoom
import org.example.virtual_rooms.app.DeleteVirtualRoom
import org.example.virtual_rooms.app.GetRoomMovies
import org.example.virtual_rooms.app.GetRoomSummary
import org.example.virtual_rooms.app.GetUserRooms
import org.example.virtual_rooms.app.JoinVirtualRoom
import org.example.virtual_rooms.app.UpdateVirtualRoom
import org.example.virtual_rooms.app.dto.CreateRoomRequest
import org.example.virtual_rooms.app.dto.JoinRoomRequest
import org.example.virtual_rooms.app.dto.RoomResponse
import org.example.virtual_rooms.app.dto.RoomSummaryResponse
import org.example.virtual_rooms.app.dto.UpdateRoomRequest
import org.example.virtual_rooms.domain.models.RoomSummary
import org.example.virtual_rooms.domain.models.VirtualRoom

fun Application.virtualRoomRoutes(
    createVirtualRoom: CreateVirtualRoom,
    joinVirtualRoom: JoinVirtualRoom,
    addMovieToRoom: AddMovieToRoom,
    getRoomMovies: GetRoomMovies,
    getRoomSummary: GetRoomSummary,
    getMovieTags: GetMovieTags,
    getUserRooms: GetUserRooms,
    updateVirtualRoom: UpdateVirtualRoom,
    deleteVirtualRoom: DeleteVirtualRoom,
    userRepository: UserRepository,
    favoriteRepository: FavoriteRepository,
    historyRepository: HistoryRepository
) {
    fun currentUserId(call: ApplicationCall): Int {
        return call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asInt()
            ?: throw IllegalArgumentException("Token invalido")
    }

    fun User?.displayName(): String? {
        return this?.username?.takeIf { it.isNotBlank() } ?: this?.email
    }

    fun VirtualRoom.toResponse(currentUserId: Int? = null): RoomResponse {
        val creatorName = userRepository.findById(creatorId).displayName()
        val guestName = guestId?.let { userRepository.findById(it).displayName() }
        val currentName = currentUserId?.let { userRepository.findById(it).displayName() }
        val partnerId = currentUserId?.let { if (creatorId == it) guestId else creatorId }
        val partnerName = partnerId?.let { userRepository.findById(it).displayName() }

        return RoomResponse(
            id = id,
            roomName = roomName,
            invitationCode = invitationCode,
            creatorId = creatorId,
            guestId = guestId,
            creatorName = creatorName,
            guestName = guestName,
            currentUserName = currentName,
            partnerName = partnerName
        )
    }

    fun RoomSummary.toResponse() = RoomSummaryResponse(
        roomId = roomId,
        roomName = roomName,
        currentUserName = currentUserName,
        partnerName = partnerName,
        listsCount = listsCount,
        favoritesCount = favoritesCount,
        ratingsCount = ratingsCount,
        averageRating = averageRating
    )

    fun Movie.toRoomResponse(roomId: Int): MovieResponse {
        val ratings = historyRepository.getByRoom(roomId).filter { it.movieId == id }
        val averageRating = ratings.takeIf { it.isNotEmpty() }?.map { it.rating }?.average()

        return MovieResponse(
            id = id,
            title = title,
            synopsis = synopsis,
            durationMinutes = durationMinutes,
            imageUrl = imageUrl,
            tags = getMovieTags.execute(id).map { TagResponse(it.id, it.name) },
            averageRating = averageRating,
            ratingCount = ratings.size,
            isFavorite = favoriteRepository.exists(roomId, id)
        )
    }

    routing {
        authenticate("auth-jwt") {
            route("/api/v1/rooms") {

                get {
                    try {
                        val currentUserId = currentUserId(call)
                        val response = getUserRooms.execute(currentUserId).map { it.toResponse(currentUserId) }

                        call.respond(HttpStatusCode.OK, response)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener tus salas"))
                    }
                }

                put("/{roomId}") {
                    try {
                        val roomId = call.parameters["roomId"]?.toInt() ?: throw Exception("ID invalido")
                        val request = call.receive<UpdateRoomRequest>()
                        val currentUserId = currentUserId(call)
                        val room = updateVirtualRoom.execute(roomId, currentUserId, request.roomName)

                        call.respond(HttpStatusCode.OK, room.toResponse(currentUserId))
                    } catch (e: IllegalAccessException) {
                        call.respond(HttpStatusCode.Forbidden, mapOf("error" to e.message))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al actualizar")))
                    }
                }

                delete("/{roomId}") {
                    try {
                        val roomId = call.parameters["roomId"]?.toInt() ?: throw Exception("ID invalido")
                        val currentUserId = currentUserId(call)

                        deleteVirtualRoom.execute(roomId, currentUserId)
                        call.respond(HttpStatusCode.OK, mapOf("message" to "Sala eliminada con exito"))
                    } catch (e: IllegalAccessException) {
                        call.respond(HttpStatusCode.Forbidden, mapOf("error" to e.message))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al eliminar")))
                    }
                }

                post {
                    try {
                        val request = call.receive<CreateRoomRequest>()
                        val currentUserId = currentUserId(call)
                        val room = createVirtualRoom.execute(request.roomName, currentUserId)

                        call.respond(HttpStatusCode.Created, room.toResponse(currentUserId))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al crear sala")))
                    }
                }

                post("/join") {
                    try {
                        val request = call.receive<JoinRoomRequest>()
                        val currentUserId = currentUserId(call)
                        val room = joinVirtualRoom.execute(request.invitationCode, currentUserId)

                        call.respond(HttpStatusCode.OK, room.toResponse(currentUserId))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al unirse")))
                    }
                }

                get("/{roomId}/summary") {
                    try {
                        val roomId = call.parameters["roomId"]?.toInt() ?: throw Exception("ID invalido")
                        val summary = getRoomSummary.execute(roomId, currentUserId(call))

                        call.respond(HttpStatusCode.OK, summary.toResponse())
                    } catch (e: IllegalAccessException) {
                        call.respond(HttpStatusCode.Forbidden, mapOf("error" to e.message))
                    } catch (e: NoSuchElementException) {
                        call.respond(HttpStatusCode.NotFound, mapOf("error" to (e.message ?: "Sala no encontrada")))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al obtener resumen")))
                    }
                }

                post("/{roomId}/movies/{movieId}") {
                    try {
                        val roomId = call.parameters["roomId"]?.toInt() ?: throw Exception("ID de sala invalido")
                        val movieId = call.parameters["movieId"]?.toInt() ?: throw Exception("ID de pelicula invalido")

                        addMovieToRoom.execute(roomId, movieId)
                        call.respond(HttpStatusCode.OK, mapOf("message" to "Pelicula agregada a la sala"))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to "No se pudo agregar"))
                    }
                }

                get("/{roomId}/movies/{movieId}") {
                    try {
                        val roomId = call.parameters["roomId"]?.toInt() ?: throw Exception("ID de sala invalido")
                        val movieId = call.parameters["movieId"]?.toInt() ?: throw Exception("ID de pelicula invalido")
                        val movie = getRoomMovies.execute(roomId).firstOrNull { it.id == movieId }
                            ?: throw NoSuchElementException("Pelicula no encontrada en esta sala")

                        call.respond(HttpStatusCode.OK, movie.toRoomResponse(roomId))
                    } catch (e: NoSuchElementException) {
                        call.respond(HttpStatusCode.NotFound, mapOf("error" to (e.message ?: "Pelicula no encontrada")))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al obtener pelicula")))
                    }
                }

                get("/{roomId}/movies") {
                    try {
                        val roomId = call.parameters["roomId"]?.toInt() ?: throw Exception("ID de sala invalido")
                        val response = getRoomMovies.execute(roomId).map { it.toRoomResponse(roomId) }

                        call.respond(HttpStatusCode.OK, response)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener cartelera"))
                    }
                }
            }
        }
    }
}
