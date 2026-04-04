package org.example.users.infra.persistence

import org.jetbrains.exposed.sql.Table

object UsersTable : Table("usuarios") {
    val id = integer("id").autoIncrement()
    val correo = varchar("correo", 255).uniqueIndex()
    val contrasena = varchar("contrasena", 255)
    val rol = varchar("rol", 50)

    override val primaryKey = PrimaryKey(id)
}