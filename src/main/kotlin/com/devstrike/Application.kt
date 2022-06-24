package com.devstrike

import com.devstrike.authentication.JWTService
import com.devstrike.authentication.hash
import com.devstrike.data.model.UserModel
import com.devstrike.repository.DatabaseFactory
import com.devstrike.repository.Repo
import com.devstrike.routes.NoteRoutes
import com.devstrike.routes.UserRoutes
import io.ktor.http.*
import io.ktor.http.HttpHeaders.ContentType
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false)
{

    //val port = System.getenv("PORT").toInt()
//        configureRouting()
//        configureSerialization()
//        configureSecurity()


        DatabaseFactory.init()

        val db = Repo()
        val jwtService = JWTService()
        val hashFunction = {s: String -> hash(s) }

        install(Sessions) {
                cookie<MySession>("MY_SESSION") {
                        cookie.extensions["SameSite"] = "lax"
                }
        }

        install(Authentication){
                jwt("jwt"){
                        verifier(jwtService.verifier)
                        realm = "Note Server"
                        validate {
                                val payload = it.payload
                                val email = payload.getClaim("email").asString()
                                val user = db.findUserByEmail(email)
                                user
                        }
                }
        }


        install(Locations)

        install(ContentNegotiation) {
                gson {
                }
        }


        //this is where all routing in the server project are defined
        routing() {
                get("/") {
                        call.respondText("Hello World!", contentType = io.ktor.http.ContentType.Text.Plain)

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

data class MySession(val count: Int = 0)

