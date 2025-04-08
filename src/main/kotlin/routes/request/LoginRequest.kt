package com.alievisa.routes.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("phone_number")
    val phoneNumber: String,
)
