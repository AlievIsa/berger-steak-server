package com.alievisa.service

import com.alievisa.utils.CustomLogger
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

    fun saveOtp(mailAddress: String, code: String) {
        otpMap[mailAddress] = code
        CustomLogger.log("OtpService: generated OTP code $code for $mailAddress was saved")

        scope.launch {
            delay(ttlSeconds * 1000)
            otpMap.remove(mailAddress)
            CustomLogger.log("OtpService: OTP code $code for $mailAddress was removed")
        }
    }

    fun verifyOtp(mailAddress: String, code: String): Boolean {
        val isValid = otpMap[mailAddress] == code
        if (isValid) {
            otpMap.remove(mailAddress)
        }
        return isValid
    }
}