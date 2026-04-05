package org.example.custom_lists.infra

import io.ktor.server.application.*
import org.example.custom_lists.app.AddMovieToCustomList
import org.example.custom_lists.app.CreateCustomList
import org.example.custom_lists.app.GetMoviesFromCustomList
import org.example.custom_lists.app.GetRoomCustomLists
import org.example.custom_lists.infra.persistence.PostgresCustomListRepository
import org.example.custom_lists.infra.routing.customListRoutes

fun Application.initCustomListsModule() {
    val repository = PostgresCustomListRepository()

    val createList = CreateCustomList(repository)
    val getLists = GetRoomCustomLists(repository)
    val addMovie = AddMovieToCustomList(repository)
    val getMovies = GetMoviesFromCustomList(repository)

    customListRoutes(createList, getLists, addMovie, getMovies)
}