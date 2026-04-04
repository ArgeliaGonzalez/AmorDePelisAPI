package org.example.virtual_rooms.infra.persistence

import org.example.virtual_rooms.domain.models.VirtualRoom
import org.example.virtual_rooms.domain.repository.VirtualRoomRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresVirtualRoomRepository : VirtualRoomRepository {

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
}