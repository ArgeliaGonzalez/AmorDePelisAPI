package org.example.tags.infra.persistence

import org.example.movies.infra.persistence.MoviesTable
import org.jetbrains.exposed.sql.Table

object TagsTable : Table("etiquetas") {
    val id = integer("id").autoIncrement()
    val nombre = varchar("nombre", 100).uniqueIndex()

    override val primaryKey = PrimaryKey(id)
}

object MovieTagsTable : Table("peliculas_etiquetas") {
    val peliculaId = integer("pelicula_id").references(MoviesTable.id)
    val etiquetaId = integer("etiqueta_id").references(TagsTable.id)

    override val primaryKey = PrimaryKey(peliculaId, etiquetaId)
}