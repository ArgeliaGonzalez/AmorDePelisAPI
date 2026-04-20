package org.example.custom_lists.infra.persistence

import org.example.custom_lists.domain.models.CustomList
import org.example.custom_lists.domain.repository.CustomListRepository
import org.example.movies.domain.models.Movie
import org.example.movies.infra.persistence.MoviesTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresCustomListRepository : CustomListRepository {

    private fun movieCount(listId: Int): Int {
        return ListMoviesTable.select { ListMoviesTable.listaId eq listId }.count().toInt()
    }

    private fun rowToCustomList(row: ResultRow) = CustomList(
        id = row[CustomListsTable.id],
        roomId = row[CustomListsTable.salaVirtualId],
        name = row[CustomListsTable.nombre],
        description = row[CustomListsTable.descripcion],
        colorHex = row[CustomListsTable.colorHex] ?: "#E91E63",
        movieCount = movieCount(row[CustomListsTable.id])
    )

    override fun create(roomId: Int, name: String, description: String?, colorHex: String?): CustomList {
        return transaction {
            val insertStmt = CustomListsTable.insert {
                it[salaVirtualId] = roomId
                it[nombre] = name
                it[descripcion] = description
                it[this.colorHex] = colorHex
            }
            CustomList(
                id = insertStmt[CustomListsTable.id],
                roomId = roomId,
                name = name,
                description = description,
                colorHex = colorHex ?: "#E91E63"
            )
        }
    }

    override fun getByRoom(roomId: Int): List<CustomList> {
        return transaction {
            CustomListsTable.select { CustomListsTable.salaVirtualId eq roomId }.map(::rowToCustomList)
        }
    }

    override fun getById(listId: Int): CustomList? {
        return transaction {
            CustomListsTable.select { CustomListsTable.id eq listId }.map(::rowToCustomList).singleOrNull()
        }
    }

    override fun countByRoom(roomId: Int): Int {
        return transaction {
            CustomListsTable.select { CustomListsTable.salaVirtualId eq roomId }.count().toInt()
        }
    }

    override fun addMovie(listId: Int, movieId: Int) {
        transaction {
            ListMoviesTable.insert {
                it[this.listaId] = listId
                it[this.peliculaId] = movieId
            }
        }
    }

    override fun getMovies(listId: Int): List<Movie> {
        return transaction {
            (ListMoviesTable innerJoin MoviesTable)
                .select { ListMoviesTable.listaId eq listId }
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
