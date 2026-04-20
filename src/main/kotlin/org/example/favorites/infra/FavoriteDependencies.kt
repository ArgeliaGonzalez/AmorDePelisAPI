package org.example.favorites.infra

import io.ktor.server.application.*
import org.example.favorites.infra.persistence.PostgresFavoriteRepository
import org.example.favorites.infra.routing.favoriteRoutes
import org.example.tags.app.GetMovieTags
import org.example.tags.infra.persistence.PostgresTagRepository

fun Application.initFavoritesModule() {
    val favoriteRepository = PostgresFavoriteRepository()
    val tagRepository = PostgresTagRepository()

    favoriteRoutes(favoriteRepository, GetMovieTags(tagRepository))
}
