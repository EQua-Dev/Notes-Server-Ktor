package com.devstrike

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.devstrike.plugins.*

fun main() {

    //val port = System.getenv("PORT").toInt()
    embeddedServer(Netty)
    {

        configureRouting()
        configureSerialization()
        configureSecurity()


    }.start(wait = true)
}
