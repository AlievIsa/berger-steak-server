package com.alievisa.data.service

import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OtpService(
    private val ttlSeconds: Long = 300
) {

    private val otpMap = ConcurrentHashMap<String, String>()
    private val scope = CoroutineScope(Dispatchers.Default)

    fun saveOtp(phoneNumber: String, code: String) {
        otpMap[phoneNumber] = code
        println("Generated OTP code $code for $phoneNumber was saved")

        scope.launch {
            delay(ttlSeconds * 1000)
            otpMap.remove(phoneNumber)
            println("Generated OTP code $code for $phoneNumber was removed")
        }
    }

    fun verifyOtp(phoneNumber: String, code: String): Boolean {
        val isValid = otpMap[phoneNumber] == code
        if (isValid) {
            otpMap.remove(phoneNumber)
        }
        return isValid
    }
}