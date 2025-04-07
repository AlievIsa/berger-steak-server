package com.alievisa.repository.impl

import com.alievisa.model.UserModel
import com.alievisa.repository.api.AuthRepository
import com.alievisa.service.JwtService
import com.alievisa.service.OtpService
import com.alievisa.service.SmsService

class AuthRepositoryImpl(
    private val jwtService: JwtService,
    private val otpService: OtpService,
    private val smsService: SmsService,
) : AuthRepository {

    override suspend fun sendOtp(phoneNumber: String) {
        val code = generateCode()
        println("Generated OTP code for $phoneNumber: $code")

        otpService.saveOtp(phoneNumber, code)
        smsService.sendSms(phoneNumber, code)
    }

    override suspend fun verifyOtp(phoneNumber: String, code: String): Boolean {
        return otpService.verifyOtp(phoneNumber, code)
    }

    override fun generateToken(userModel: UserModel) = jwtService.generateToken(userModel)

    override fun getJwtVerifier() = jwtService.getVerifier()

    private fun generateCode(): String {
        return (1000..9999).random().toString()
    }
}