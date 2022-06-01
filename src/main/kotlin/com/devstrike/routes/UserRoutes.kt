package com.devstrike.routes

import com.devstrike.authentication.JWTService
import com.devstrike.data.model.LoginRequest
import com.devstrike.data.model.RegisterRequest
import com.devstrike.data.model.SimpleResponse
import com.devstrike.data.model.UserModel
import com.devstrike.repository.Repo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.locations.post
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

//to avoid hard coding and appear professional 😉 we assign the api route details to a constant variable and use Locations
const val API_VERSION = "/v1"
const val USERS = "$API_VERSION/users"
const val REGISTER_REQUEST = "$USERS/register"
const val LOGIN_REQUEST = "$USERS/login"

@Location(REGISTER_REQUEST)
class UserRegisterRoute

@Location(LOGIN_REQUEST)
class UserLoginRoute


//kotlin file to contain all user related routes
//extend the 'Routes' library to the file name and pass the db, jwt service and password hash function as parameters
fun Route.UserRoutes(
    db: Repo,
    jwtService: JWTService,
    hashFunction: (String) -> String
){

    post<UserRegisterRoute> {
        //use try catch in case the user doesn't satisfy the registration parameters
        val registerRequest = try {
            //receive data with the parameters satisfying RegisterRequest data class
            call.receive<RegisterRequest>()
        } catch (e:Exception){
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Fields"))
            return@post
        }

        try {
            //add the registered user to db
            val user = UserModel(registerRequest.email, hashFunction(registerRequest.password), registerRequest.name)
            db.addUser(user)
            //send jwtoken to user in response body
            call.respond(HttpStatusCode.OK, SimpleResponse(true, jwtService.generateToken(user)))
        }catch (e: Exception){
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message?: "Some Problem Occurred"))
        }

    }

    post<UserLoginRoute> {
        val loginRequest = try {
            call.receive<LoginRequest>()
        }catch (e: Exception){
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Some Fields"))
            return@post
        }

        try {
//            val email = loginRequest.email
//            db.findUserByEmail(email)

            //checks if user email exists in the database
            val user = db.findUserByEmail(loginRequest.email)

            if (user == null){
                //if user does not exist, send error message
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Wrong Email Id"))
            }else{
                //if user exists check if...
                if (user.hashPassword == hashFunction(loginRequest.password)){
                    //...the password is correct and give response
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, jwtService.generateToken(user)))
                }else{
                    //...the password is incorrect and give error message
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Password Incorrect!"))
                }
            }

//            call.respond(HttpStatusCode.OK, SimpleResponse(true, "User Exists"))
        }catch (e: Exception){
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some Problem Occurred"))
            return@post
        }
    }

}