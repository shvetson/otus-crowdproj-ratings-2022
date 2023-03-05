import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

group = "com.crowdproj.marketplace:ratings"
version = "0.0.1"

allprojects {
    repositories{
        google()
        mavenCentral()
        maven{ url = uri("https://jitpack.io")}
    }
}

subprojects {

    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
}