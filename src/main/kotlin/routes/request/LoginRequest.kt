package com.alievisa.routes.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val mail: String,
)
