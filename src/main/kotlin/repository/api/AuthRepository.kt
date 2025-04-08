package com.alievisa.repository.api

import com.auth0.jwt.JWTVerifier

interface AuthRepository {

    suspend fun sendOtp(phoneNumber: String)

    suspend fun verifyOtp(phoneNumber: String, code: String): Boolean

    fun generateAccessToken(userId: Int): String

    fun generateRefreshToken(userId: Int): String

    fun getAccessVerifier(): JWTVerifier

    fun getRefreshVerifier(): JWTVerifier

    suspend fun saveRefreshToken(userId: Int, token: String)

    suspend fun getRefreshTokenByUserId(userId: Int): String?

    suspend fun deleteRefreshTokenByUserId(userId: Int)
}