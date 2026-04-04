package org.example.movies.infra.persistence

import org.jetbrains.exposed.sql.Table

object MoviesTable : Table("peliculas") {
    val id = integer("id").autoIncrement()
    val titulo = varchar("titulo", 255)
    val sinopsis = text("sinopsis").nullable()
    val duracionMinutos = integer("duracion_minutos").nullable()
    val imagenUrl = text("imagen_url").nullable()

    override val primaryKey = PrimaryKey(id)
}