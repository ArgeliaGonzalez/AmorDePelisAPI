package org.example.virtual_rooms.infra

import io.ktor.server.application.*
import org.example.virtual_rooms.app.AddMovieToRoom
import org.example.virtual_rooms.app.CreateVirtualRoom
import org.example.virtual_rooms.app.GetRoomMovies
import org.example.virtual_rooms.app.GetRoomSummary
import org.example.virtual_rooms.app.JoinVirtualRoom
import org.example.custom_lists.infra.persistence.PostgresCustomListRepository
import org.example.favorites.infra.persistence.PostgresFavoriteRepository
import org.example.history.infra.persistence.PostgresHistoryRepository
import org.example.tags.app.GetMovieTags
import org.example.tags.infra.persistence.PostgresTagRepository
import org.example.users.infra.persistence.PostgresUserRepository
import org.example.virtual_rooms.infra.persistence.PostgresVirtualRoomRepository
import org.example.virtual_rooms.infra.routing.virtualRoomRoutes
import org.example.virtual_rooms.app.GetUserRooms
import org.example.virtual_rooms.app.UpdateVirtualRoom
import org.example.virtual_rooms.app.DeleteVirtualRoom

fun Application.initVirtualRoomModule() {
    val repository = PostgresVirtualRoomRepository()
    val userRepository = PostgresUserRepository()
    val customListRepository = PostgresCustomListRepository()
    val favoriteRepository = PostgresFavoriteRepository()
    val historyRepository = PostgresHistoryRepository()
    val tagRepository = PostgresTagRepository()

    val createRoom = CreateVirtualRoom(repository)
    val joinRoom = JoinVirtualRoom(repository)
    val addMovieToRoom = AddMovieToRoom()
    val getRoomMovies = GetRoomMovies()
    val getRoomSummary = GetRoomSummary(repository, userRepository, customListRepository, favoriteRepository, historyRepository)
    val getMovieTags = GetMovieTags(tagRepository)
    val getUserRooms = GetUserRooms(repository)
    val updateRoom = UpdateVirtualRoom(repository)
    val deleteRoom = DeleteVirtualRoom(repository)

    virtualRoomRoutes(
        createRoom,
        joinRoom,
        addMovieToRoom,
        getRoomMovies,
        getRoomSummary,
        getMovieTags,
        getUserRooms,
        updateRoom,
        deleteRoom,
        userRepository,
        favoriteRepository,
        historyRepository
    )
}
