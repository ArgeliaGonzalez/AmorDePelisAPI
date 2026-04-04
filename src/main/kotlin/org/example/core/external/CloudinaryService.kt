package org.example.core.external

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import org.example.core.database.DatabaseFactory
import java.io.File

object CloudinaryService {

    private val cloudinary = Cloudinary(DatabaseFactory.getEnv("CLOUDINARY_URL"))

    fun uploadImage(file: File, folderName: String): String {
        val params = ObjectUtils.asMap(
            "folder", "amor_de_pelis/$folderName",
            "use_filename", true,
            "unique_filename", true
        )

        val uploadResult = cloudinary.uploader().upload(file, params)

        return uploadResult["secure_url"] as String
    }
}