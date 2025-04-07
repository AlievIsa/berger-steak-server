package com.alievisa.data.repository.impl

import com.alievisa.data.repository.api.AuthRepository
import com.alievisa.data.service.OtpService
import com.alievisa.data.service.SmsSender

class AuthRepositoryImpl(
    private val otpService: OtpService,
    private val smsSender: SmsSender,
) : AuthRepository {

    override suspend fun sendOtp(phoneNumber: String) {
        val code = generateCode()
        println("Generated OTP code for $phoneNumber: $code")

        otpService.saveOtp(phoneNumber, code)
        smsSender.sendSms(phoneNumber, code)
    }

    override suspend fun verifyOtp(phoneNumber: String, code: String): Boolean {
        return otpService.verifyOtp(phoneNumber, code)
    }

    private fun generateCode(): String {
        return (1000..9999).random().toString()
    }
}