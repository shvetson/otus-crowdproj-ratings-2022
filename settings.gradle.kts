//rootProject.name = "otus-crowdproj-ratings-2022"
rootProject.name = "ratings"

pluginManagement {
    val kotlinVersion: String by settings
    val kotestVersion: String by settings
    val openapiVersion: String by settings // для open api генерации


    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
        id("io.kotest.multiplatform") version kotestVersion apply false

        // для open api генерации
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
    }
}

include("api-v1")
include("common")
include("mappers-v1")