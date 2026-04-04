package org.example.virtual_rooms.app

import org.example.virtual_rooms.infra.persistence.RoomMoviesTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class AddMovieToRoom {
    fun execute(roomId: Int, movieId: Int) {
        transaction {
            RoomMoviesTable.insert {
                it[salaId] = roomId
                it[peliculaId] = movieId
            }
        }
    }
}