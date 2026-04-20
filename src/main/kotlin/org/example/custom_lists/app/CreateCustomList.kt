package org.example.custom_lists.app

import org.example.custom_lists.domain.models.CustomList
import org.example.custom_lists.domain.repository.CustomListRepository

class CreateCustomList(private val repository: CustomListRepository) {
    fun execute(roomId: Int, name: String, description: String? = null, colorHex: String? = null): CustomList {
        if (name.isBlank()) throw IllegalArgumentException("El nombre de la lista no puede estar vacio")
        return repository.create(roomId, name, description, colorHex)
    }
}
