package com.alievisa.routes

import com.alievisa.repository.api.MenuRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.menuRoute(menuRepository: MenuRepository) {

    get("api/v1/get-menu") {
        val menu = menuRepository.getMenu()
        call.respond(HttpStatusCode.OK, menu)
    }
}