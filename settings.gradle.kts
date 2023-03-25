rootProject.name = "ratings"

pluginManagement {
    val kotlinVersion: String by settings
    val kotestVersion: String by settings

    // open api
    val openapiVersion: String by settings

    // spring
    val springframeworkBootVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val pluginSpringVersion: String by settings
    val pluginJpa: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
        id("io.kotest.multiplatform") version kotestVersion apply false

        // open api
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("org.openapi.generator") version openapiVersion apply false

        // spring
        id("org.springframework.boot") version springframeworkBootVersion apply false
        id("io.spring.dependency-management") version springDependencyManagementVersion apply false
        kotlin("plugin.spring") version pluginSpringVersion apply false
        kotlin("plugin.jpa") version pluginJpa apply false
    }
}

include("testing")
include("api-v1")
include("common")
include("mappers-v1")
include("stubs")
include("spring")
