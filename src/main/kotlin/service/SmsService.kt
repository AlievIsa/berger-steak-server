package com.alievisa.service

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable

interface SmsService {

    suspend fun sendSms(destination: String, code: String)
}

class ExolveSmsService : SmsService {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }
    private val senderNumber = System.getenv("SMS_SENDER_NUMBER") ?: throw IllegalStateException("SMS_SENDER_NUMBER is not set")
    private val apiKey = System.getenv("EXOLVE_API_KEY") ?: throw IllegalStateException("EXOLVE_API_KEY is not set")

    override suspend fun sendSms(destination: String, code: String) {
        val response = client.post("https://api.exolve.ru/messaging/v1/SendSMS") {
            headers {
                append("Authorization", apiKey)
                append("Content-Type", "application/json")
            }
            setBody(
                SendSmsBody(
                    number = senderNumber,
                    destination = destination,
                    text = code,
                )
            )
        }

        if (!response.status.isSuccess()) {
            throw RuntimeException("Failed to send SMS: ${response.status}")
        }
    }
}

@Serializable
data class SendSmsBody(
    val number: String,
    val destination: String,
    val text: String,
)
