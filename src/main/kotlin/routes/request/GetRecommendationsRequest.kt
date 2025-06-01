package com.alievisa.routes.request

import com.alievisa.model.BasketModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRecommendationsRequest(
    @SerialName("basket_model")
    val basketModel: BasketModel,
)
