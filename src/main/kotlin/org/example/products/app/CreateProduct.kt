package org.example.products.app

import org.example.core.external.CloudinaryService
import org.example.products.domain.models.Product
import org.example.products.domain.repository.ProductRepository
import java.io.File

class CreateProduct(private val repository: ProductRepository) {
    fun execute(name: String, description: String?, imageFile: File?): Product {
        if (name.isBlank()) throw IllegalArgumentException("El nombre del producto es obligatorio")

        var imageUrl: String? = null
        if (imageFile != null) {
            imageUrl = CloudinaryService.uploadImage(imageFile, "productos")
            imageFile.delete()
        }

        val product = Product(id = 0, name = name, description = description, imageUrl = imageUrl)
        return repository.save(product)
    }
}