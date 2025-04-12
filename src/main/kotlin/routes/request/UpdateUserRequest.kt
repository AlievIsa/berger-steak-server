package com.alievisa.routes.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequest(
    val name: String,
    @SerialName("phone_number")
    val phoneNumber: String,
    val address: String,
)