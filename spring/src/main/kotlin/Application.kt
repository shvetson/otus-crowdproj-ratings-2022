package com.crowdproj.rating.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    val ctx = runApplication<Application>(*args)
    println("""
        count = ${ctx.beanDefinitionCount}
    """.trimIndent())
}