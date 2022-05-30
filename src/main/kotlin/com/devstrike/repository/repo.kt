package com.devstrike.repository

import com.devstrike.data.model.UserModel
import com.devstrike.data.table.UserTable
import com.devstrike.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select


//this class will contain the functions to be used to query the database
class repo {

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

}