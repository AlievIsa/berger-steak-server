package com.alievisa.routes.request

import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpRequest(
    val mail: String,
    val code: String,
)
