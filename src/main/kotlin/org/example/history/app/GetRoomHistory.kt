package org.example.history.app

import org.example.history.domain.models.WatchedMovie
import org.example.history.domain.repository.HistoryRepository

class GetRoomHistory(private val repository: HistoryRepository) {
    fun execute(roomId: Int): List<WatchedMovie> {
        return repository.getByRoom(roomId)
    }
}