package org.example.history.infra.persistence

import org.example.movies.infra.persistence.MoviesTable
import org.example.virtual_rooms.infra.persistence.VirtualRoomsTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object HistoryTable : Table("historial_vistas") {
    val id = integer("id").autoIncrement()
    val salaVirtualId = integer("sala_virtual_id").references(VirtualRoomsTable.id)
    val peliculaId = integer("pelicula_id").references(MoviesTable.id)
    val fechaVista = date("fecha_vista")
    val calificacion = integer("calificacion")

    override val primaryKey = PrimaryKey(id)
}