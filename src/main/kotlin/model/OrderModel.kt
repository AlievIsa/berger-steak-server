package com.alievisa.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class OrderModel(
    val id: Int = 0,
    val restaurantId: Int,
    val price: Int,
    val timestamp: Instant,
    val status: String,
    val positions: List<PositionModel>,
)