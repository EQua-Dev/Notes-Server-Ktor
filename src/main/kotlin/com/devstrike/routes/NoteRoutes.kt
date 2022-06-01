package com.devstrike.routes

import com.devstrike.data.model.NoteModel
import com.devstrike.data.model.SimpleResponse
import com.devstrike.data.model.UserModel
import com.devstrike.repository.Repo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.locations.post
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val NOTES = "$API_VERSION/notes"
const val CREATE_NOTES = "$NOTES/new_note"
const val UPDATE_NOTES = "$NOTES/update"
const val DELETE_NOTES = "$NOTES/delete"

@Location(NOTES)
class NoteGetRoute

@Location(CREATE_NOTES)
class NoteCreateRoute

@Location(UPDATE_NOTES)
class NoteUpdateRoute

@Location(DELETE_NOTES)
class NoteDeleteRoute

fun Route.NoteRoutes(
    db: Repo,
    hashFunction: (String) -> String
){

    //implement all the routes within the "authenticate" block to run the authentications automatically
    //we write the name of the authentication we want to perform in parentheses and if multiple, we write them in comma separated strings
    authenticate("jwt") {

        post<NoteCreateRoute> {
            val note = try {
                call.receive<NoteModel>()
            }catch (e: Exception){
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Fields"))
                return@post
            }

            try {
                //get the sender's email from the user model class as the principal for the authentication
                val email = call.principal<UserModel>()!!.email
                //add the note to database
                db.addNote(note, email)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note Added Successfully!"))

            }catch (e: Exception){
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, "Some Error Occurred!"))
            }
        }

        get<NoteGetRoute> {

            try {
               val email = call.principal<UserModel>()!!.email
               val notes = db.getAllNotes(email)
               call.respond(HttpStatusCode.OK, notes)
            }catch (e: Exception){
                call.respond(HttpStatusCode.Conflict, e.message ?: "Some Error Occurred!")
            }

        }

        post<NoteUpdateRoute> {

            val note = try {
                call.receive<NoteModel>()
            }catch (e: Exception){
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Fields!"))
                return@post
            }

            try {
                //get the sender's email from the user model class as the principal for the authentication
                val email = call.principal<UserModel>()!!.email
                //add the note to database
                db.updateNote(note, email)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note Updated Successfully!"))

            }catch (e: Exception){
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, "Some Error Occurred"))
            }

        }

        delete<NoteDeleteRoute> {

            val noteId = try {
                //request that the request route contain the id for the note to be deleted
                call.request.queryParameters["id"]!!
            }catch (e: Exception){
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "QueryParameter: id is not present"))
                return@delete
            }

            try {

                val email = call.principal<UserModel>()!!.email
                db.deleteNote(noteId, email)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note Deleted Successfully!"))
            }catch (e: Exception){
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some Problem Occurred!"))
            }

        }

    }
}