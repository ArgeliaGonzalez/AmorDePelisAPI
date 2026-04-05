package org.example.products.infra.persistence

import org.jetbrains.exposed.sql.Table

object ProductsTable : Table("productos") {
    val id = integer("id").autoIncrement()
    val nombre = varchar("nombre", 255)
    val descripcion = text("descripcion").nullable()
    val imagenUrl = text("imagen_url").nullable()

    override val primaryKey = PrimaryKey(id)
}