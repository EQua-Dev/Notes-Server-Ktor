package com.devstrike.data.model

//data model class for notes table
data class NoteModel(
    val id: String,
    val noteTitle: String,
    val description: String,
    val date: Long
)
