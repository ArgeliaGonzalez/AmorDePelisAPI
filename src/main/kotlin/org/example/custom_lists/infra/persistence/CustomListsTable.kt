package org.example.custom_lists.infra.persistence

import org.example.movies.infra.persistence.MoviesTable
import org.example.virtual_rooms.infra.persistence.VirtualRoomsTable
import org.jetbrains.exposed.sql.Table

object CustomListsTable : Table("listas_personalizadas") {
    val id = integer("id").autoIncrement()
    val salaVirtualId = integer("sala_virtual_id").references(VirtualRoomsTable.id)
    val nombre = varchar("nombre", 150)
    val descripcion = text("descripcion").nullable()
    val colorHex = varchar("color_hex", 20).nullable()

    override val primaryKey = PrimaryKey(id)
}

object ListMoviesTable : Table("listas_peliculas") {
    val listaId = integer("lista_id").references(CustomListsTable.id)
    val peliculaId = integer("pelicula_id").references(MoviesTable.id)

    override val primaryKey = PrimaryKey(listaId, peliculaId)
}
