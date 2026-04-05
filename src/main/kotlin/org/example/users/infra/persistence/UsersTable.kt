package org.example.users.infra.persistence

import org.jetbrains.exposed.sql.Table

object UsersTable : Table("usuarios") {
    val id = integer("id").autoIncrement()
    val correo = varchar("correo", 255).uniqueIndex()
    val contrasena = varchar("contrasena", 255)
    val rol = varchar("rol", 50)
    val username = varchar("username", 100).nullable()
    val fotoPerfil = text("foto_perfil").nullable()

    override val primaryKey = PrimaryKey(id)
}