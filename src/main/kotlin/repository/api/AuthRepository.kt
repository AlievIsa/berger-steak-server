package com.alievisa.repository.api

import com.auth0.jwt.JWTVerifier

interface AuthRepository {

    suspend fun sendOtp(mail: String)

    suspend fun verifyOtp(mail: String, code: String): Boolean

    fun generateAccessToken(userId: Int): String

    fun generateRefreshToken(deviceId: String): String

    fun getAccessVerifier(): JWTVerifier

    fun getRefreshVerifier(): JWTVerifier

    suspend fun getUserIdByDeviceId(deviceId: String): Int?

    suspend fun saveRefreshToken(deviceId: String, userId: Int, token: String)

    suspend fun checkIsDeviceIdSuspiciouslyUsed(deviceId: String): Boolean

    suspend fun getRefreshTokenByDeviceId(deviceId: String): String?

    suspend fun deleteRefreshTokenByDeviceId(deviceId: String)
}