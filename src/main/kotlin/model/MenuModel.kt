package com.alievisa.model

import kotlinx.serialization.Serializable

@Serializable
data class MenuModel(
    val categories: List<CategoryModel>,
)