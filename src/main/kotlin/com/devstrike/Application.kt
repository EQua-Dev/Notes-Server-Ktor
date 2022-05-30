package com.devstrike

import com.devstrike.authentication.JWTService
import com.devstrike.authentication.hash
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.devstrike.plugins.*
import com.devstrike.repository.DatabaseFactory
import com.devstrike.repository.repo

fun main() {
    embeddedServer(Netty, port = 8000, host = "localhost") {
        configureRouting()
        configureSerialization()
        configureSecurity()


    }.start(wait = true)
}
