package com.alievisa.model

import kotlinx.serialization.Serializable

@Serializable
data class PositionModel(
    val id: Int,
    val dishId: Int,
    val dishAmount: Int,
)