rootProject.name = "ratings"

pluginManagement {
    val kotlinVersion: String by settings

    // test
    val kotestVersion: String by settings

    // open api
    val openapiVersion: String by settings

    // spring
    val springframeworkBootVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val pluginSpringVersion: String by settings
    val pluginJpa: String by settings

    // ktor
    val ktorPluginVersion: String by settings

    // docker
    val bmuschkoVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion

        // test
        id("io.kotest.multiplatform") version kotestVersion apply false

        // open api
        id("org.openapi.generator") version openapiVersion apply false

        // spring
        id("org.springframework.boot") version springframeworkBootVersion apply false
        id("io.spring.dependency-management") version springDependencyManagementVersion apply false
        kotlin("plugin.spring") version pluginSpringVersion apply false
        kotlin("plugin.jpa") version pluginJpa apply false

        // ktor
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("io.ktor.plugin") version ktorPluginVersion apply false

        // docker
        id("com.bmuschko.docker-java-application") version bmuschkoVersion apply false
        id("com.bmuschko.docker-remote-api") version bmuschkoVersion apply false
    }
}

include("testing")
include("api-v1")
include("common")
include("mappers-v1")
include("stubs")
include("spring")
include("ktor")
include("kafka")
include("biz")
