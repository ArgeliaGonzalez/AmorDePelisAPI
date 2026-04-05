package org.example.news.infra.persistence

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object NewsTable : Table("noticias") {
    val id = integer("id").autoIncrement()
    val titulo = varchar("titulo", 255)
    val contenido = text("contenido")
    val fechaPublicacion = date("fecha_publicacion")
    val imagenUrl = text("imagen_url").nullable()

    override val primaryKey = PrimaryKey(id)
}