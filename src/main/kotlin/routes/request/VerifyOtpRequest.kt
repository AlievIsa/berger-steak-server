package com.alievisa.routes.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpRequest(
    val mail: String,
    val code: String,
    @SerialName("device_id")
    val deviceId: String,
)
