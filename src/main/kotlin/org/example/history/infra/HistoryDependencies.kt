package org.example.history.infra

import io.ktor.server.application.*
import org.example.history.app.AddMovieToHistory
import org.example.history.app.GetRoomHistory
import org.example.history.infra.persistence.PostgresHistoryRepository
import org.example.history.infra.routing.historyRoutes

fun Application.initHistoryModule() {
    val repository = PostgresHistoryRepository()

    val addMovieToHistory = AddMovieToHistory(repository)
    val getRoomHistory = GetRoomHistory(repository)

    historyRoutes(addMovieToHistory, getRoomHistory)
}