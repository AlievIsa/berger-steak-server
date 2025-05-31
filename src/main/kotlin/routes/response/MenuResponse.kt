package com.alievisa.routes.response

import com.alievisa.model.MenuModel
import kotlinx.serialization.Serializable

@Serializable
data class MenuResponse(
    val menu: MenuModel,
)