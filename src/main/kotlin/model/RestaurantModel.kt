package com.alievisa.model

import kotlinx.serialization.Serializable

@Serializable
data class RestaurantModel(
    val id: Int = 0,
    val address: String,
    val location: String,
)
