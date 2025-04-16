package com.alievisa.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object CustomLogger {

    private val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    fun log(message: String) {
        val timestamp = LocalDateTime.now().format(timeFormatter)
        println("$timestamp $message")
    }
}