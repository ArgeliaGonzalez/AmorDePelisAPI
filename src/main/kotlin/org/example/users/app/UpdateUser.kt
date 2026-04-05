package org.example.users.app
import org.example.core.external.CloudinaryService
import org.example.users.domain.models.User
import org.example.users.domain.repository.UserRepository
import java.io.File

class UpdateUser(private val repository: UserRepository) {
    fun execute(id: Int, username: String?, imageFile: File?): User {
        val currentUser = repository.findById(id) ?: throw Exception("Usuario no encontrado")

        var newImageUrl = currentUser.profileImageUrl

        if (imageFile != null) {
            newImageUrl = CloudinaryService.uploadImage(imageFile, "perfiles")
            imageFile.delete()
        }

        val updatedUser = currentUser.copy(
            username = username ?: currentUser.username,
            profileImageUrl = newImageUrl
        )

        return repository.update(updatedUser)
    }
}