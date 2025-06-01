package com.alievisa.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BasketModel(
    val positions: List<PositionModel>,
    @SerialName("total_price")
    val totalPrice: Int,
)
