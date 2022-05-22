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
    routing() {
        get("/") {
            call.respondText("Hello World!", contentType = ContentType.Text.Plain)

        }

        //path parameters
        get("/note/{id}"){
            val id = call.parameters["id"]
            call.respond(id.toString())
        }

        //query parameters
        get("/note") {
            val id = call.request.queryParameters["id"]
            call.respond(id.toString())
        }

        post ("/note") {
            //in a post request we have to receive a body, thus below is how to define the expected body to receive
            val body = call.receive<String>()
            call.respond(body)//passes the defines body in the response
        }

        // the parameters can be passed for two routes with the same path in this format
        route("/notes"){

            //route nesting
            route("/create"){
                //localhost:8080/notes
                post {
                    //in a post request we have to receive a body, thus below is how to define the expected body to receive
                    val body = call.receive<String>()
                    call.respond(body)//passes the defines body in the response
                }
            }


            delete {
                val body = call.receive<String>()
                call.respond(body)
            }
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
