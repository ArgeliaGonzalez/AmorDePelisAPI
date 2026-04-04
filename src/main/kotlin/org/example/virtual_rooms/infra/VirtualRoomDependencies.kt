package org.example.virtual_rooms.infra

import io.ktor.server.application.*
import org.example.virtual_rooms.app.AddMovieToRoom
import org.example.virtual_rooms.app.CreateVirtualRoom
import org.example.virtual_rooms.app.GetRoomMovies
import org.example.virtual_rooms.app.JoinVirtualRoom
import org.example.virtual_rooms.infra.persistence.PostgresVirtualRoomRepository
import org.example.virtual_rooms.infra.routing.virtualRoomRoutes

fun Application.initVirtualRoomModule() {
    val repository = PostgresVirtualRoomRepository()

    val createRoom = CreateVirtualRoom(repository)
    val joinRoom = JoinVirtualRoom(repository)
    val addMovieToRoom = AddMovieToRoom()
    val getRoomMovies = GetRoomMovies()

    virtualRoomRoutes(createRoom, joinRoom, addMovieToRoom, getRoomMovies)
}