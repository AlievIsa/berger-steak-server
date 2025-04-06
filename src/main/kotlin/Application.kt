package com.alievisa

import com.alievisa.plugins.configureDatabase
import com.alievisa.plugins.configureMonitoring
import com.alievisa.plugins.configureRouting
import com.alievisa.plugins.configureSecurity
import com.alievisa.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    configureDatabase()
    configureSerialization()
    configureMonitoring()
    configureSecurity()
    configureRouting()
}
