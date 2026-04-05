package org.example.tags.infra

import io.ktor.server.application.*
import org.example.tags.app.*
import org.example.tags.infra.persistence.PostgresTagRepository
import org.example.tags.infra.routing.tagRoutes

fun Application.initTagsModule() {
    val repository = PostgresTagRepository()

    tagRoutes(
        CreateTag(repository),
        GetAllTags(repository),
        AddTagToMovie(repository),
        GetMovieTags(repository)
    )
}