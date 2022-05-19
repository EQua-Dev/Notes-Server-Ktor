package com.devstrike.plugins

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.locations.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {
    install(Locations) {
    }

    //this is where all routing in the server project are defined
    routing {
        get("/") {
            call.respondText("Hello World!", contentType = ContentType.Text.Plain)

        }

        //localhost:8080/notes
        post("/notes") {
            //in a post request we have to receive a body, thus below is how to define the expected body to receive

        }

    }
}
@Location("/location/{name}")
class MyLocation(val name: String, val arg1: Int = 42, val arg2: String = "default")
@Location("/type/{name}") data class Type(val name: String) {
    @Location("/edit")
    data class Edit(val type: Type)

    @Location("/list/{page}")
    data class List(val type: Type, val page: Int)
}
