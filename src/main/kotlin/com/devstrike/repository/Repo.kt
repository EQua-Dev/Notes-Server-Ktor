package com.devstrike.repository

import com.devstrike.data.model.NoteModel
import com.devstrike.data.model.UserModel
import com.devstrike.data.table.NoteTable
import com.devstrike.data.table.UserTable
import com.devstrike.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*


//this class will contain the functions to be used to query the database
class Repo {

    //function to add a new user to the database
    //suspend function because the operation function being used (dbQuery) is a suspend function
    suspend fun addUser(user: UserModel) {
        dbQuery {
            UserTable.insert { ut ->
                ut[UserTable.email] = user.email
                ut[UserTable.hashPassword] = user.hashPassword
                ut[UserTable.name] = user.userName

            }
        }
    }

    //function to search for a user in the database by email
    suspend fun findUserByEmail(email: String) = dbQuery {
        UserTable.select { UserTable.email.eq(email) }
            .map { rowToUser(it) }
            .singleOrNull()

        //since we know that the result will only yield either only one user or none

        //the query will return a result row (the entire row of the found email)
        // and so we will create a converter function to convert the result row to a user model class
    }

    //function to convert the result row to the user model class
    private fun rowToUser(row: ResultRow?): UserModel? {
        if (row == null) {
            return null
        }

        return UserModel(
            email = row[UserTable.email],
            hashPassword = row[UserTable.hashPassword],
            userName = row[UserTable.name]
        )
    }



//    ==================== NOTES =================

    //function to add new note to the database
    suspend fun addNote(note: NoteModel, email: String){

        dbQuery {
            NoteTable.insert { nt ->
                nt[NoteTable.id] = note.id
                nt[NoteTable.userEmail] = email
                nt[NoteTable.noteTitle] = note.noteTitle
                nt[NoteTable.description] = note.description
                nt[NoteTable.date] = note.date

            }
        }
    }

    //function to fetch all the notes belonging to a user, returns a list
    suspend fun getAllNotes(email: String): List<NoteModel> = dbQuery {
        NoteTable.select{
            //selects only the notes that the email column are that of the requesting email
            NoteTable.userEmail.eq(email)
        }.mapNotNull {rowToNote(it)}
    }

    //function to update a particular note
    suspend fun updateNote(note: NoteModel, email: String){

        dbQuery {

            NoteTable.update(
                where = {
                    //ensures the request satisfies the conditions of the requested owner email and note id
                    NoteTable.userEmail.eq(email) and NoteTable.id.eq(note.id)
                }
            ) { nt->
                //re-inserts the notes
                nt[NoteTable.id] = note.id
                nt[NoteTable.noteTitle] = note.noteTitle
                nt[NoteTable.description] = note.description
                nt[NoteTable.date] = note.date
            }

        }

    }

    //function to delete selected note
    suspend fun deleteNote(id: String, email: String){
        dbQuery {
            //before delete, check for the note with the entered email and note id
            NoteTable.deleteWhere {NoteTable.userEmail.eq(email) and NoteTable.id.eq(id)}
        }
    }

    //function to convert the retrieved db table row to object
    private fun rowToNote(row: ResultRow?): NoteModel?{
        if (row == null){
            return null
        }

        return NoteModel(
            id = row[NoteTable.id],
            noteTitle = row[NoteTable.noteTitle],
            description = row[NoteTable.description],
            date = row[NoteTable.date]

        )
    }

}