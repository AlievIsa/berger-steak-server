package com.alievisa.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PositionModel(
    val id: Int = 0,
    @SerialName("dish_id")
    val dishId: Int,
    @SerialName("dish_amount")
    val dishAmount: Int,
)