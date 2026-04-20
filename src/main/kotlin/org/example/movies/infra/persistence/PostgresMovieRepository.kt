package org.example.movies.infra.persistence

import org.example.movies.domain.models.Movie
import org.example.movies.domain.repository.MovieRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.ResultRow

class PostgresMovieRepository : MovieRepository {

    private fun rowToMovie(row: ResultRow) = Movie(
        id = row[MoviesTable.id],
        title = row[MoviesTable.titulo],
        synopsis = row[MoviesTable.sinopsis],
        durationMinutes = row[MoviesTable.duracionMinutos],
        imageUrl = row[MoviesTable.imagenUrl]
    )

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

    override fun findAll(): List<Movie> {
        return transaction {
            MoviesTable.selectAll().map(::rowToMovie)
        }
    }

    override fun findById(id: Int): Movie? {
        return transaction {
            MoviesTable.select { MoviesTable.id eq id }.map(::rowToMovie).singleOrNull()
        }
    }

    override fun searchByTitle(query: String): List<Movie> {
        return transaction {
            MoviesTable.select { MoviesTable.titulo.lowerCase() like "%${query.lowercase()}%" }.map(::rowToMovie)
        }
    }
}
