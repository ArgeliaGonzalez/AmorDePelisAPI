package org.example.history.domain.repository

import org.example.history.domain.models.WatchedMovie

interface HistoryRepository {
    fun add(roomId: Int, movieId: Int, rating: Int)
    fun getByRoom(roomId: Int): List<WatchedMovie>
}