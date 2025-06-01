package com.alievisa.routes

import com.alievisa.repository.api.RecommendationsRepository
import com.alievisa.routes.request.GetRecommendationsRequest
import com.alievisa.routes.response.RecommendationsResponse
import com.alievisa.utils.Constants
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.recommendationsRoute(recommendationsRepository: RecommendationsRepository) {

    post("v1/get-recommendations") {
        val request = call.receiveNullable<GetRecommendationsRequest>() ?: run {
            call.respond(HttpStatusCode.BadRequest, Constants.ERROR.BAD_REQUEST)
            return@post
        }

        val recommendations = recommendationsRepository.getRecommendations(request.basketModel)

        call.respond(HttpStatusCode.OK, RecommendationsResponse(recommendations))
    }
}