val ktorVersion: String by project
val logbackVersion: String by project
val serializationVersion: String by project
val ktorPluginVersion: String by project
val kotlinVersion: String by project

fun ktorServer(module: String, version: String? = this@Build_gradle.ktorVersion): Any =
    "io.ktor:ktor-server-$module:$version"
fun ktorClient(module: String, version: String? = this@Build_gradle.ktorVersion): Any =
    "io.ktor:ktor-client-$module:$version"

plugins {
    id("application")
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("io.ktor.plugin")

    // docker
    id("com.bmuschko.docker-java-application")
    id("com.bmuschko.docker-remote-api")
}

repositories {
    maven {
        url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
    }  // подключение репозитория с ktor плагинами
}

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
//    mainClass.set("io.ktor.server.netty.EngineMain")
//    mainClass.set("com.crowdproj.rating.ktor.ApplicationKt")
}

dependencies {
    implementation(kotlin("stdlib-common"))
    implementation("io.ktor:ktor-server-core:$ktorVersion")

    implementation("io.ktor:ktor-server-cio:$ktorVersion")
//    implementation("io.ktor:ktor-server-netty:$ktorVersion")

    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")

    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")

    implementation("io.ktor:ktor-server-double-receive:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-server-config-yaml:$ktorVersion")

    // тесты, в тч ktor-client-content-negotiation
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")

    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")

    // модули приложения
    implementation(project(":common"))
    implementation(project(":api-v1"))
    implementation(project(":mappers-v1"))
    implementation(project(":stubs"))

    implementation(project(":biz"))
    implementation(project(":lib-logging-common"))
    implementation(project(":lib-logging-logback"))
    implementation(project(":mappers-log"))
    implementation(project(":api-log"))
    implementation(project(":repo-in-memory"))
}

//tasks {
//    // написание docker файла
//    val dockerJvmDockerfile by creating(Dockerfile::class) {
//        group = "docker"
//        from("openjdk:17")
//        copyFile("app.jar", "app.jar")
//        entryPoint("java", "-Xms256m", "-Xmx512m", "-jar", "/app.jar")
//    }
//
//    // создание image
//    create("dockerBuildJvmImage", DockerBuildImage::class) {
//        group = "docker"
//        dependsOn(dockerJvmDockerfile, named("jvmJar"))
//        doFirst {
//            copy {
//                from(named("jvmJar"))
//                into("${project.buildDir}/docker/app.jar")
//            }
//        }
//        images.add("${project.name}:${project.version}")
//    }
//}