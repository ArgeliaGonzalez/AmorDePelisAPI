package org.example.virtual_rooms.infra.persistence

import org.example.virtual_rooms.domain.models.VirtualRoom
import org.example.virtual_rooms.domain.repository.VirtualRoomRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PostgresVirtualRoomRepository : VirtualRoomRepository {

    private fun resultRowToVirtualRoom(it: ResultRow) = VirtualRoom(
        id = it[VirtualRoomsTable.id],
        roomName = it[VirtualRoomsTable.nombreSala],
        invitationCode = it[VirtualRoomsTable.codigoInvitacion],
        creatorId = it[VirtualRoomsTable.creadorId],
        guestId = it[VirtualRoomsTable.invitadoId]
    )

    override fun save(room: VirtualRoom): VirtualRoom {
        return transaction {
            val insertStmt = VirtualRoomsTable.insert {
                it[nombreSala] = room.roomName
                it[codigoInvitacion] = room.invitationCode
                it[creadorId] = room.creatorId
            }
            room.copy(id = insertStmt[VirtualRoomsTable.id])
        }
    }

    override fun findByCode(code: String): VirtualRoom? {
        return transaction {
            VirtualRoomsTable.select { VirtualRoomsTable.codigoInvitacion eq code }
                .map {
                    VirtualRoom(
                        id = it[VirtualRoomsTable.id],
                        roomName = it[VirtualRoomsTable.nombreSala],
                        invitationCode = it[VirtualRoomsTable.codigoInvitacion],
                        creatorId = it[VirtualRoomsTable.creadorId],
                        guestId = it[VirtualRoomsTable.invitadoId]
                    )
                }.singleOrNull()
        }
    }

    override fun updateGuest(roomId: Int, guestId: Int): VirtualRoom {
        return transaction {
            VirtualRoomsTable.update({ VirtualRoomsTable.id eq roomId }) {
                it[invitadoId] = guestId
            }
            VirtualRoomsTable.select { VirtualRoomsTable.id eq roomId }.map {
                VirtualRoom(
                    id = it[VirtualRoomsTable.id],
                    roomName = it[VirtualRoomsTable.nombreSala],
                    invitationCode = it[VirtualRoomsTable.codigoInvitacion],
                    creatorId = it[VirtualRoomsTable.creadorId],
                    guestId = it[VirtualRoomsTable.invitadoId]
                )
            }.single()
        }
    }

    override fun findById(id: Int): VirtualRoom? {
        return transaction {
            VirtualRoomsTable.select { VirtualRoomsTable.id eq id }
                .map(::resultRowToVirtualRoom)
                .singleOrNull()
        }
    }

    override fun findByUserId(userId: Int): List<VirtualRoom> {
        return transaction {
            // Buscamos las salas donde sea el creador O el invitado
            VirtualRoomsTable.select {
                (VirtualRoomsTable.creadorId eq userId) or (VirtualRoomsTable.invitadoId eq userId)
            }.map(::resultRowToVirtualRoom)
        }
    }

    override fun updateName(roomId: Int, newName: String): VirtualRoom {
        return transaction {
            VirtualRoomsTable.update({ VirtualRoomsTable.id eq roomId }) {
                it[nombreSala] = newName
            }
            findById(roomId)!!
        }
    }

    override fun delete(roomId: Int) {
        transaction {
            VirtualRoomsTable.deleteWhere { VirtualRoomsTable.id eq roomId }
        }
    }
}