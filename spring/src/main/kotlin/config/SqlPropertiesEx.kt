package com.crowdproj.rating.spring.config

import com.crowdproj.rating.repo.postgresql.SqlProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

// Нужна аннотация @ConstructorBinding, ее нельзя поставить над методом c @Bean, не нашел другого пути
@ConfigurationProperties("sql")
class SqlPropertiesEx
    @ConstructorBinding
    constructor(
        url: String,
        user: String,
        password: String,
        schema: String,
        dropDatabase: Boolean
    ): SqlProperties(url, user, password, schema, dropDatabase)