val ktorVersion: String by project
val logbackVersion: String by project
val serializationVersion: String by project
val ktorPluginVersion: String by project
val kotlinVersion: String by project
val javaVersion: String by project
val datetimeVersion: String by project

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

val webjars: Configuration by configurations.creating
dependencies {
    val swaggerUiVersion: String by project
    webjars("org.webjars:swagger-ui:$swaggerUiVersion")
}

repositories {
    maven {
        url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
    }  // подключение репозитория с ktor плагинами
}

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

jib {
    container { mainClass = application.mainClass.get() }
}

ktor {
    docker {
        localImageName.set(project.name)
        imageTag.set(project.version.toString())
        jreVersion.set(io.ktor.plugin.features.JreVersion.valueOf("JRE_$javaVersion"))
    }
}

dependencies {
    implementation(kotlin("stdlib-common"))
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-cio:$ktorVersion")

    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")

    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

    implementation("io.ktor:ktor-server-double-receive:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-server-config-yaml:$ktorVersion")

    implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt:$ktorVersion")

    implementation("io.ktor:ktor-server-auto-head-response:$ktorVersion")
    implementation("io.ktor:ktor-server-caching-headers:$ktorVersion")
//    implementation("io.ktor:ktor-cors:$ktorVersion")
    api("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")

//    implementation("io.ktor:ktor-auth:$ktorVersion")
//    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")

//    implementation("com.sndyuk:logback-more-appenders:1.8.8")
//    implementation("org.fluentd:fluent-logger:0.3.4")

    // тесты, в тч ktor-client-content-negotiation
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")

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

    implementation(project(":repo-stubs"))
    implementation(project(":repo-in-memory"))
    implementation(project(":repo-postgresql"))
    implementation(project(":repo-cassandra"))
    implementation(project(":repo-gremlin"))
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

tasks {
    shadowJar {
        isZip64 = true
    }

    @Suppress("UnstableApiUsage")
    withType<ProcessResources>().configureEach {
        println("RESOURCES: ${this.name} ${this::class}")
        from("$rootDir/specs") {
            into("specs")
            filter {
                // Устанавливаем версию в сваггере
                it.replace("\${VERSION_APP}", project.version.toString())
            }
        }
        webjars.forEach { jar ->
//        emptyList<File>().forEach { jar ->
            val conf = webjars.resolvedConfiguration
            println("JarAbsPa: ${jar.absolutePath}")
            val artifact = conf.resolvedArtifacts.find { it.file.toString() == jar.absolutePath } ?: return@forEach
            val upStreamVersion = artifact.moduleVersion.id.version.replace("(-[\\d.-]+)", "")
            copy {
                from(zipTree(jar))
                duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                into(file("${buildDir}/webjars-content/${artifact.name}"))
            }
            with(this@configureEach) {
                this.duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                from(
                    "${buildDir}/webjars-content/${artifact.name}/META-INF/resources/webjars/${artifact.name}/${upStreamVersion}"
                ) { into(artifact.name) }
                from(
                    "${buildDir}/webjars-content/${artifact.name}/META-INF/resources/webjars/${artifact.name}/${artifact.moduleVersion.id.version}"
                ) { into(artifact.name) }
            }
        }
    }
}