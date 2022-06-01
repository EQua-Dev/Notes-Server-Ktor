package com.devstrike.data.model


//data model class for the user register request
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String
)
