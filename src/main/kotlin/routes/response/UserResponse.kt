package com.alievisa.routes.response

import com.alievisa.model.UserModel
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val user: UserModel,
)