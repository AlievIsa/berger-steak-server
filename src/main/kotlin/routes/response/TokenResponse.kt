package com.alievisa.routes.response

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token: String,
)
