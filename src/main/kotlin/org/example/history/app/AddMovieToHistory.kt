package org.example.history.app

import org.example.history.domain.repository.HistoryRepository

class AddMovieToHistory(private val repository: HistoryRepository) {
    fun execute(roomId: Int, movieId: Int, rating: Int) {
        if (rating !in 1..5) {
            throw IllegalArgumentException("La calificación debe estar entre 1 y 5")
        }
        repository.add(roomId, movieId, rating)
    }
}