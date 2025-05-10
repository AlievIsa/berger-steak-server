package com.alievisa.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DishModel(
    val id: Int = 0,
    @SerialName("category_id")
    val categoryId: Int,
    val name: String,
    val price: Int,
    val description: String,
    val image: String,
    val weight: Int,
    val calories: Int,
)