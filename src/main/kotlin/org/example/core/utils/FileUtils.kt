package org.example.core.utils

import io.ktor.http.content.*
import java.io.File
import java.util.UUID

fun PartData.FileItem.saveToTempFile(): File {
    val fileBytes = streamProvider().readBytes()
    val tempFile = File.createTempFile("upload_", "_${UUID.randomUUID()}_${originalFileName}")
    tempFile.writeBytes(fileBytes)
    return tempFile
}