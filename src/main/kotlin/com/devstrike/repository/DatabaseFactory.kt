package com.devstrike.repository

import com.devstrike.data.table.NoteTable
import com.devstrike.data.table.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URI

//import org.jetbrains.exposed.sql.Database


object DatabaseFactory {

    //function to connect to our database using the datasource below
    fun init() {
        Database.connect(hikari())

        //transaction to create the user table in the database
        transaction {
            SchemaUtils.create(UserTable)
            SchemaUtils.create(NoteTable)
        }
    }

    //function to provide us with the data source of our database
    fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = System.getenv("JDBC_DRIVER") // 1
//        config.jdbcUrl = System.getenv("DATABASE_URL") // 2
        config.maximumPoolSize = 3 //maximum connections to our database is 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"

        val uri = URI(System.getenv("DATABASE_URL"))
        val username = uri.userInfo.split(":").toTypedArray()[0]
        val password = uri.userInfo.split(":").toTypedArray()[1]

//        config.jdbcUrl = "jdbc:postgresql://" + uri.host + uri.path + "?sslmode=require" + "&user=$username&password=$password"

        config.validate()

        return HikariDataSource(config)
    }

//    postgres://wnlnntnhcuesxv:03262494b297ed9facb5c7daeed8c87df3bb80fd1bb495ba76abf41d29aa3b95@ec2-34-231-221-151.compute-1.amazonaws.com:5432/db9nml45lfg8vl


    //suspend function to perform queries in coroutines scope
    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }

}