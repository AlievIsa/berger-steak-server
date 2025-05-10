package com.alievisa.routes.request

import com.alievisa.model.PositionModel
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderRequest(
    @SerialName("restaurant_id")
    val restaurantId: Int,
    val price: Int,
    val timestamp: Instant,
    val positions: List<PositionModel>,
)