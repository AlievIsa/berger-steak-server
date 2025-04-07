package com.alievisa.routes.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequest(
    val name: String,
    val address: String,
)