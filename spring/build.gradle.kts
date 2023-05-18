plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.serialization")
}

dependencies {
    val kotestVersion: String by project
    val springdocOpenapiUiVersion: String by project
    val coroutinesVersion: String by project
    val serializationVersion: String by project

    implementation("org.springframework.boot:spring-boot-starter-actuator") // info; refresh; springMvc output
//    implementation("org.springframework.boot:spring-boot-starter-web") // Controller, Service, etc..
    implementation("org.springframework.boot:spring-boot-starter-webflux") // Controller, Service, etc..
    implementation("org.springframework.boot:spring-boot-starter-websocket") // Controller, Service, etc..
    implementation("org.springdoc:springdoc-openapi-ui:$springdocOpenapiUiVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin") // from models to json and Vice versa
    implementation("org.jetbrains.kotlin:kotlin-reflect") // for spring-boot app
    implementation("org.jetbrains.kotlin:kotlin-stdlib") // for spring-boot app
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutinesVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${coroutinesVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:${coroutinesVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

    // transport models
    implementation(project(":common"))

    // v1 api
    implementation(project(":api-v1"))
    implementation(project(":mappers-v1"))

    // stubs
    implementation(project(":stubs"))

    // biz
    implementation(project(":biz"))

    // logging
    implementation(project(":lib-logging-common"))
    implementation(project(":lib-logging-logback"))
    implementation(project(":mappers-log"))
    implementation(project(":api-log"))

    // repository
    implementation(project(":repo-in-memory"))
    implementation(project(":repo-postgresql"))
    implementation(project(":repo-tests"))
    implementation(project(":repo-stubs"))

    // tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux") // Controller, Service, etc..
    testImplementation("com.ninja-squad:springmockk:3.0.1") // mockking beans
}

tasks {
    withType<ProcessResources> {
        from("$rootDir/specs") {
            into("/static")
        }
    }
}

/*sourceSets {
    main {
        resources {
            srcDirs("$rootDir/specs")
            println(srcDirs)
        }
    }
}*/

tasks.withType<Test> {
    useJUnitPlatform()
}
