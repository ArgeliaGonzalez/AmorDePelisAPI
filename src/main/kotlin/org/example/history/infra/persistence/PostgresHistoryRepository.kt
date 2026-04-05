package org.example.history.infra.persistence

import org.example.history.domain.models.WatchedMovie
import org.example.history.domain.repository.HistoryRepository
import org.example.movies.infra.persistence.MoviesTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

class PostgresHistoryRepository : HistoryRepository {

    override fun add(roomId: Int, movieId: Int, rating: Int) {
        transaction {
            HistoryTable.insert {
                it[salaVirtualId] = roomId
                it[peliculaId] = movieId
                it[fechaVista] = LocalDate.now()
                it[calificacion] = rating
            }
        }
    }

    override fun getByRoom(roomId: Int): List<WatchedMovie> {
        return transaction {
            (HistoryTable innerJoin MoviesTable)
                .select { HistoryTable.salaVirtualId eq roomId }
                .orderBy(HistoryTable.fechaVista, org.jetbrains.exposed.sql.SortOrder.DESC) // Las más recientes primero
                .map {
                    WatchedMovie(
                        historyId = it[HistoryTable.id],
                        movieId = it[MoviesTable.id],
                        title = it[MoviesTable.titulo],
                        imageUrl = it[MoviesTable.imagenUrl],
                        rating = it[HistoryTable.calificacion],
                        watchedDate = it[HistoryTable.fechaVista].toString()
                    )
                }
        }
    }
}