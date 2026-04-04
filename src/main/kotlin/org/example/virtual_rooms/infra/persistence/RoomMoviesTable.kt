package org.example.virtual_rooms.infra.persistence

import org.example.movies.infra.persistence.MoviesTable
import org.jetbrains.exposed.sql.Table

object RoomMoviesTable : Table("salas_peliculas") {
    val salaId = integer("sala_id").references(VirtualRoomsTable.id)
    val peliculaId = integer("pelicula_id").references(MoviesTable.id)

    override val primaryKey = PrimaryKey(salaId, peliculaId)
}