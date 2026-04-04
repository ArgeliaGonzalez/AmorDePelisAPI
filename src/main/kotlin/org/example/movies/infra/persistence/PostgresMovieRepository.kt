package org.example.movies.infra.persistence

import org.example.movies.domain.models.Movie
import org.example.movies.domain.repository.MovieRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresMovieRepository : MovieRepository {
    override fun save(movie: Movie): Movie {
        return transaction {
            val insertStmt = MoviesTable.insert {
                it[titulo] = movie.title
                it[sinopsis] = movie.synopsis
                it[duracionMinutos] = movie.durationMinutes
                it[imagenUrl] = movie.imageUrl
            }
            movie.copy(id = insertStmt[MoviesTable.id])
        }
    }
}