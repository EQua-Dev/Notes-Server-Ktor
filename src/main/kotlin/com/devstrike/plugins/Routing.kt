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


    val db = Repo()
    val jwtService = JWTService()
    val hashFunction = {s: String -> hash(s)}


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
