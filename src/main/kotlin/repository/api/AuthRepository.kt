package com.alievisa.repository.api

import com.alievisa.model.UserModel
import com.auth0.jwt.JWTVerifier

interface AuthRepository {

    suspend fun sendOtp(phoneNumber: String)

    suspend fun verifyOtp(phoneNumber: String, code: String): Boolean

    fun generateToken(userModel: UserModel): String

    fun getJwtVerifier(): JWTVerifier
}