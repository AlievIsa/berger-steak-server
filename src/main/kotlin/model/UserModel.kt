package com.alievisa.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val id: Int = 0,
    val name: String,
    @SerialName("phone_number")
    val phoneNumber: String,
    val address: String,
)