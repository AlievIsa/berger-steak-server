package com.alievisa.routes.response

import com.alievisa.model.DishModel
import kotlinx.serialization.Serializable

@Serializable
data class RecommendationsResponse(
    val dishes: List<DishModel>,
)
