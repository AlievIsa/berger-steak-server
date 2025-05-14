package com.alievisa.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderModel(
    val id: Int = 0,
    @SerialName("restaurant_id")
    val restaurantId: Int,
    val price: Int,
    val timestamp: Instant,
    val status: OrderStatus,
    val positions: List<PositionModel>,
)

enum class OrderStatus(val value: String) {
    CREATED("Created"),
    PROCESSING("Processing"),
    CANCELED("Canceled"),
    DONE("Done"),
    RECEIVED("Received");

    companion object {
        fun statusFromString(value: String): OrderStatus? =
            entries.find { it.value.equals(value, ignoreCase = true) }
    }
}