package com.alievisa.model

import kotlinx.serialization.Serializable

@Serializable
data class DishModel(
    val id: Int = 0,
    val name: String,
    val price: Int,
    val description: String,
    val image: String,
)
