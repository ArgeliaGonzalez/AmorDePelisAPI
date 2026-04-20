package org.example.virtual_rooms.app

import org.example.custom_lists.domain.repository.CustomListRepository
import org.example.favorites.domain.repository.FavoriteRepository
import org.example.history.domain.repository.HistoryRepository
import org.example.users.domain.models.User
import org.example.users.domain.repository.UserRepository
import org.example.virtual_rooms.domain.models.RoomSummary
import org.example.virtual_rooms.domain.repository.VirtualRoomRepository

class GetRoomSummary(
    private val virtualRoomRepository: VirtualRoomRepository,
    private val userRepository: UserRepository,
    private val customListRepository: CustomListRepository,
    private val favoriteRepository: FavoriteRepository,
    private val historyRepository: HistoryRepository
) {
    fun execute(roomId: Int, currentUserId: Int): RoomSummary {
        val room = virtualRoomRepository.findById(roomId) ?: throw NoSuchElementException("Sala no encontrada")
        if (room.creatorId != currentUserId && room.guestId != currentUserId) {
            throw IllegalAccessException("No perteneces a esta sala")
        }

        val currentUser = userRepository.findById(currentUserId)
        val partnerId = if (room.creatorId == currentUserId) room.guestId else room.creatorId
        val partner = partnerId?.let { userRepository.findById(it) }
        val history = historyRepository.getByRoom(roomId)
        val averageRating = if (history.isEmpty()) 0.0 else history.map { it.rating }.average()

        return RoomSummary(
            roomId = room.id,
            roomName = room.roomName,
            currentUserName = currentUser.displayName(),
            partnerName = partner.displayNameOrNull(),
            listsCount = customListRepository.countByRoom(roomId),
            favoritesCount = favoriteRepository.countByRoom(roomId),
            ratingsCount = history.size,
            averageRating = averageRating
        )
    }

    private fun User?.displayName(): String {
        return this?.username?.takeIf { it.isNotBlank() } ?: this?.email ?: "Usuario"
    }

    private fun User?.displayNameOrNull(): String? {
        return this?.username?.takeIf { it.isNotBlank() } ?: this?.email
    }
}
