package com.devstrike

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.devstrike.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSerialization()
        configureSecurity()
    }.start(wait = true)
}
