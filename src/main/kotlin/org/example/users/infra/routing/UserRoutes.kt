package org.example.users.infra.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.userRoutes(

) {
    routing {
        route("/users") {
            post("/register") {
            }
            post("/login") {
            }
        }
    }
}