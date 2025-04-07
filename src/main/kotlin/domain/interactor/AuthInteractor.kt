package com.alievisa.domain.interactor

import com.alievisa.authentication.JwtService
import com.alievisa.data.repository.api.AuthRepository
import com.alievisa.domain.model.UserModel

class AuthInteractor(
    private val jwtService: JwtService,
    private val repository: AuthRepository,
) {

    suspend fun sendOtp(phoneNumber: String) = repository.sendOtp(phoneNumber)

    suspend fun verifyOtp(phoneNumber: String, code: String) = repository.verifyOtp(phoneNumber, code)

    fun generateToken(userModel: UserModel) = jwtService.generateToken(userModel)

    fun getJwtVerifier() = jwtService.getVerifier()
}