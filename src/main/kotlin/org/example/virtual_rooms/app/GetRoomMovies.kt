package org.example.virtual_rooms.app

import org.example.movies.domain.models.Movie
import org.example.movies.infra.persistence.MoviesTable
import org.example.virtual_rooms.infra.persistence.RoomMoviesTable
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class GetRoomMovies {
    fun execute(roomId: Int): List<Movie> {
        return transaction {
            (RoomMoviesTable innerJoin MoviesTable)
                .select { RoomMoviesTable.salaId eq roomId }
                .map {
                    Movie(
                        id = it[MoviesTable.id],
                        title = it[MoviesTable.titulo],
                        synopsis = it[MoviesTable.sinopsis],
                        durationMinutes = it[MoviesTable.duracionMinutos],
                        imageUrl = it[MoviesTable.imagenUrl]
                    )
                }
        }
    }
}