package org.example.virtual_rooms.infra.persistence

import org.example.users.infra.persistence.UsersTable
import org.jetbrains.exposed.sql.Table

object VirtualRoomsTable : Table("salas_virtuales") {
    val id = integer("id").autoIncrement()
    val nombreSala = varchar("nombre_sala", 150)
    val codigoInvitacion = varchar("codigo_invitacion", 50).uniqueIndex()
    // Referencias foráneas a la tabla de usuarios
    val creadorId = integer("creador_id").references(UsersTable.id)
    val invitadoId = integer("invitado_id").references(UsersTable.id).nullable()

    override val primaryKey = PrimaryKey(id)
}