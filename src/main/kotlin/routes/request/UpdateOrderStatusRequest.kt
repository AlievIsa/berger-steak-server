package com.alievisa.routes.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateOrderStatusRequest(
    @SerialName("order_id")
    val orderId: Int,
    val status: String,
)
