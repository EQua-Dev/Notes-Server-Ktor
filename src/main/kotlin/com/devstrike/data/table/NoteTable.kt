package com.devstrike.data.table

import org.jetbrains.exposed.sql.Table

object NoteTable: Table() {

    val id = varchar("id", 512)
    val userEmail = varchar("userEmail", 512).references(UserTable.email)//create a reference to the user email in the user table
    val noteTitle = text("noteTitle") //text is used when the entry is to have infinite length
    val description = text("description")
    val date = long("date")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}