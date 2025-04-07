package com.alievisa.data.model.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpRequest(
    @SerialName("phone_number")
    val phoneNumber: String,
    val code: String,
)
