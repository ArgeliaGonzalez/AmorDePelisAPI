package org.example.core.middleware

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.withRole(vararg allowedRoles: String, build: Route.() -> Unit) {
    authenticate("auth-jwt") {
        intercept(ApplicationCallPipeline.Call) {
            val principal = call.principal<JWTPrincipal>()
            val userRole = principal?.payload?.getClaim("role")?.asString()

            if (userRole == null || userRole !in allowedRoles) {
                call.respond(HttpStatusCode.Forbidden, mapOf("error" to "No tienes permisos para esta acción"))
                finish()
            }
        }
        build()
    }
}