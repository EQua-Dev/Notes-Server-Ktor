val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val postgres_version: String by project
val hikari_version: String by project
val h2_version: String by project

plugins {
    application
    kotlin("jvm") version "1.6.21"

}

group = "com.devstrike"
version = "0.0.1"
application {
    mainClass.set("com.devstrike.ApplicationKt")




    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}
//sourceSets {
//    main.kotlin.srcDirs = [ 'src/main/kotlin' ]
//    main.resources.srcDirs = [ 'src/main/resource' ]
//}

tasks.create("stage"){
    dependsOn("installDist")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}


dependencies {
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-gson-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-locations-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-sessions-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")


    implementation ("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation ("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation ("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
//    implementation ("org.jetbrains.exposed:exposed:$exposed_version")
    implementation ("org.postgresql:postgresql:$postgres_version")
    implementation ("com.zaxxer:HikariCP:$hikari_version")
    implementation("com.h2database:h2:$h2_version")
}

//val exposed_version: String by project
//val h2_version: String by project
//
//dependencies {
//    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
//    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
//    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
//    implementation("com.h2database:h2:$h2_version")
//}