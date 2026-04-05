package org.example.custom_lists.infra.persistence

import org.example.custom_lists.domain.models.CustomList
import org.example.custom_lists.domain.repository.CustomListRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.example.movies.infra.persistence.MoviesTable
import org.example.movies.domain.models.Movie

class PostgresCustomListRepository : CustomListRepository {

    override fun create(roomId: Int, name: String): CustomList {
        return transaction {
            val insertStmt = CustomListsTable.insert {
                it[salaVirtualId] = roomId
                it[nombre] = name
            }
            CustomList(
                id = insertStmt[CustomListsTable.id],
                roomId = roomId,
                name = name
            )
        }
    }

    override fun getByRoom(roomId: Int): List<CustomList> {
        return transaction {
            CustomListsTable.select { CustomListsTable.salaVirtualId eq roomId }.map {
                CustomList(
                    id = it[CustomListsTable.id],
                    roomId = it[CustomListsTable.salaVirtualId],
                    name = it[CustomListsTable.nombre]
                )
            }
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