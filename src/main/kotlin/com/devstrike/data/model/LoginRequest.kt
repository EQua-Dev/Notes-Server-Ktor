package com.devstrike.data.model

//data model class for the user login request
data class LoginRequest(
    val email: String,
    val password: String
)
