plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {}
    macosX64 {}
    linuxX64 {}

    sourceSets {
        val coroutinesVersion: String by project

        all { languageSettings.optIn("kotlin.RequiresOptIn") }

        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))

                val kotlinCorVersion: String by project
                implementation("com.crowdproj:kotlin-cor:$kotlinCorVersion")

                implementation(project(":common"))
                implementation(project(":stubs"))
//                implementation(project(":lib-cor"))
//                implementation(project(":repo-in-memory"))
                implementation(project(":lib-logging-common"))
                implementation(project(":auth"))
            }
        }
        @Suppress("UNUSED_VARIABLE")
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

//                implementation(project(":repo-stubs"))
                implementation(project(":repo-tests"))
                implementation(project(":repo-in-memory"))

                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
            }
        }
        @Suppress("UNUSED_VARIABLE")
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }
        @Suppress("UNUSED_VARIABLE")
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}
