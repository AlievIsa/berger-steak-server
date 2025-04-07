package com.alievisa.data.repository.api

interface AuthRepository {

    suspend fun sendOtp(phoneNumber: String)

    suspend fun verifyOtp(phoneNumber: String, code: String): Boolean
}