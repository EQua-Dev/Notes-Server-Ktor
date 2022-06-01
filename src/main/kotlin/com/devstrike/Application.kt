package com.devstrike

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.devstrike.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false)
{

    //val port = System.getenv("PORT").toInt()
        configureRouting()
        configureSerialization()
        configureSecurity()

}
