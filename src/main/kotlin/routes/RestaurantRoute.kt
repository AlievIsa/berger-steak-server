package com.alievisa.routes

import com.alievisa.repository.api.RestaurantRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.restaurantRoute(restaurantRepository: RestaurantRepository) {

    get("api/v1/get-restaurants") {
        val restaurants = restaurantRepository.getRestaurants()
        call.respond(HttpStatusCode.OK, restaurants)
    }
}