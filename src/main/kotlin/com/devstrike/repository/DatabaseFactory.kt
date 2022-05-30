package com.devstrike.repository

import com.devstrike.data.table.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

//import org.jetbrains.exposed.sql.Database


object DatabaseFactory {

    //function to connect to our database using the datasource below
    fun init() {
        Database.connect(hikari())

        //transaction to create the user table in the database
        transaction {
            SchemaUtils.create(UserTable)
        }
    }

    //function to provide us with the data source of our database
    fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = System.getenv("JDBC_DRIVER") // 1
        config.jdbcUrl = System.getenv("DATABASE_URL") // 2
        config.maximumPoolSize = 3 //maximum connections to our database is 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()

        return HikariDataSource(config)
    }


    //suspend function to perform queries in coroutines scope
    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }

}