package org.example.favorites.infra.persistence

import org.example.favorites.domain.repository.FavoriteRepository
import org.example.movies.domain.models.Movie
import org.example.movies.infra.persistence.MoviesTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresFavoriteRepository : FavoriteRepository {

    override fun add(roomId: Int, movieId: Int) {
        transaction {
            val alreadyExists = FavoritesTable.select {
                (FavoritesTable.salaVirtualId eq roomId) and (FavoritesTable.peliculaId eq movieId)
            }.count() > 0

            if (!alreadyExists) {
                FavoritesTable.insert {
                    it[salaVirtualId] = roomId
                    it[peliculaId] = movieId
                }
            }
        }
    }

    override fun remove(roomId: Int, movieId: Int) {
        transaction {
            FavoritesTable.deleteWhere {
                (salaVirtualId eq roomId) and (peliculaId eq movieId)
            }
        }
    }

    override fun exists(roomId: Int, movieId: Int): Boolean {
        return transaction {
            FavoritesTable.select {
                (FavoritesTable.salaVirtualId eq roomId) and (FavoritesTable.peliculaId eq movieId)
            }.count() > 0
        }
    }

    override fun getByRoom(roomId: Int): List<Movie> {
        return transaction {
            (FavoritesTable innerJoin MoviesTable)
                .select { FavoritesTable.salaVirtualId eq roomId }
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

    override fun countByRoom(roomId: Int): Int {
        return transaction {
            FavoritesTable.select { FavoritesTable.salaVirtualId eq roomId }.count().toInt()
        }
    }
}
