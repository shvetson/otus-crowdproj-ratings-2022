plugins {
    kotlin("jvm")
    id("org.openapi.generator")
}

dependencies {
    val jacksonVersion: String by project
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    testImplementation(kotlin("test-junit"))
}

/**
 * Указываем место куда будут сгенерированы модели
 */
sourceSets {
    main {
        java.srcDir("$buildDir/generate-resources/main/src/main/kotlin")
    }
}

/**
 * Настраиваем генерацию здесь
 */
openApiGenerate {
    val openapiGroup = "${rootProject.group}.api.v1"
    generatorName.set("kotlin") // Это и есть активный генератор (клиентский)
    packageName.set(openapiGroup)
    apiPackage.set("$openapiGroup.api")
    modelPackage.set("$openapiGroup.models")
    invokerPackage.set("$openapiGroup.invoker")
    inputSpec.set("$rootDir/specs/specs-ratings-v1.yaml") //место спецификации

    /**
     * Здесь указываем, что нам нужны только модели, все остальное не нужно
     * https://openapi-generator.tech/docs/globals
     */
    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
    }

    /**
     * Настройка дополнительных параметров из документации по генератору
     * https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/kotlin.md
     */
    configOptions.set(
        mapOf(
            "dateLibrary" to "string",
            "enumPropertyNaming" to "UPPERCASE",
            "serializationLibrary" to "jackson",
            "collectionType" to "list"
        )
    )
}

/**
 * Указываем, что модели генерятся до компиляции, те сначала генерируем модели, потом компилируем код
 */
tasks {
    compileKotlin {
        dependsOn(openApiGenerate)
    }
}
