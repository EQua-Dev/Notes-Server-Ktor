package com.devstrike.data.model

//data class to define the various values to be defined for the user
//NOTE: this class does not create the table in the database
data class UserModel(
    val email: String,
    val hashPassword: String,
    val userName: String
)
