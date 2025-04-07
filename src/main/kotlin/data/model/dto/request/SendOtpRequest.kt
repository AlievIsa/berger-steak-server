package com.alievisa.data.model.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendOtpRequest(
    @SerialName("phone_number")
    val phoneNumber: String,
)
