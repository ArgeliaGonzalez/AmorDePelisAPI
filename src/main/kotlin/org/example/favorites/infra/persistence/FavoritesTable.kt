package org.example.favorites.infra.persistence

import org.example.movies.infra.persistence.MoviesTable
import org.example.virtual_rooms.infra.persistence.VirtualRoomsTable
import org.jetbrains.exposed.sql.Table

object FavoritesTable : Table("peliculas_favoritas") {
    val salaVirtualId = integer("sala_virtual_id").references(VirtualRoomsTable.id)
    val peliculaId = integer("pelicula_id").references(MoviesTable.id)

    override val primaryKey = PrimaryKey(salaVirtualId, peliculaId)
}
