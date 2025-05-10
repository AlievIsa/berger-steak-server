package com.alievisa.routes.response

import com.alievisa.model.RestaurantModel
import kotlinx.serialization.Serializable

@Serializable
data class RestaurantsResponse(
    val restaurants: List<RestaurantModel>,
)
