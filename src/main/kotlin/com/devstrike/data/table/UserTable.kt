package com.devstrike.data.table

import org.jetbrains.exposed.sql.Table

//object to create the table, its columns and properties
//the object extends a Table class from exposed library
//it is object because there will be only one instance of it
object UserTable:Table("users") {

    //define the columns and its properties
    val email = varchar("email", 512)
    val name = varchar("name", 512)
    val hashPassword = varchar("hashPassword", 512)

    //to make email a primary key, override primary key from the exposed Table class
    override val primaryKey: PrimaryKey = PrimaryKey(email)

}