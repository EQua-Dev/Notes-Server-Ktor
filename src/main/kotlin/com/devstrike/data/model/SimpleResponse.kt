package com.devstrike.data.model


//data model class for the response we send to the requests
data class SimpleResponse(
    val success: Boolean,
    val message: String
)
