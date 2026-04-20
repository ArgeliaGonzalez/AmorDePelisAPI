package org.example.custom_lists.app

import org.example.custom_lists.domain.models.CustomList
import org.example.custom_lists.domain.repository.CustomListRepository

class GetCustomList(private val repository: CustomListRepository) {
    fun execute(listId: Int): CustomList {
        return repository.getById(listId) ?: throw NoSuchElementException("Lista no encontrada")
    }
}
