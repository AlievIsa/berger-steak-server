package com.alievisa.routes

import com.alievisa.repository.api.MenuRepository
import com.alievisa.routes.response.MenuResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.menuRoute(menuRepository: MenuRepository) {

    get("v1/get-menu") {
        val menu = menuRepository.getMenu()
        call.respond(HttpStatusCode.OK, MenuResponse(menu))
    }
}