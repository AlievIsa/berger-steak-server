package com.alievisa.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryModel(
    val id: Int = 0,
    val name: String,
    val dishes: List<DishModel>,
)