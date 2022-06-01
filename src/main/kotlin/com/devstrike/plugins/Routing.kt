package com.devstrike.plugins

import com.devstrike.authentication.JWTService
import com.devstrike.authentication.hash
import com.devstrike.data.model.UserModel
import com.devstrike.repository.Repo
import com.devstrike.routes.NoteRoutes
import com.devstrike.routes.UserRoutes
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.locations.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {

    install(Locations) {
    }

    val db = Repo()
    val jwtService = JWTService()
    val hashFunction = {s: String -> hash(s)}


    //this is where all routing in the server project are defined
    routing() {
        get("/") {
            call.respondText("Hello World!", contentType = ContentType.Text.Plain)

        }

        UserRoutes(db, jwtService, hashFunction)
        NoteRoutes(db, hashFunction)

        //path parameters
        get("/note/{id}"){
            val id = call.parameters["id"]
            call.respond(id.toString())
        }

        get("/token") {
            //collect the parameters entered by the user and store to variables
            val email = call.request.queryParameters["email"]!!
            val password = call.request.queryParameters["password"]!!
            val username = call.request.queryParameters["username"]!!


            val user = UserModel(email, hashFunction(password), username)

            //return the token to the user in the response body
            call.respond(jwtService.generateToken(user))
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
//@Location("/location/{name}")
//class MyLocation(val name: String, val arg1: Int = 42, val arg2: String = "default")
//@Location("/type/{name}") data class Type(val name: String) {
//    @Location("/edit")
//    data class Edit(val type: Type)
//
//    @Location("/list/{page}")
//    data class List(val type: Type, val page: Int)
//}
