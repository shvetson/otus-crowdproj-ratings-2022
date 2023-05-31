package com.crowdproj.rating.ktor.plugin

import com.crowdproj.rating.common.repo.IRatingRepository
import com.crowdproj.rating.ktor.config.CassandraConfig
import com.crowdproj.rating.ktor.config.ConfigPaths
import com.crowdproj.rating.ktor.config.GremlinConfig
import com.crowdproj.rating.ktor.config.PostgresConfig
import com.crowdproj.rating.repo.cassandra.RepoRatingCassandra
import com.crowdproj.rating.repo.gremlin.RatingRepoGremlin
import com.crowdproj.rating.repo.inmemory.RatingRepoInMemory
import com.crowdproj.rating.repo.postgresql.RepoRatingSQL
import com.crowdproj.rating.repo.postgresql.SqlProperties
import io.ktor.server.application.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

//actual fun Application.getDatabaseConf(type: RatingDbType): IRatingRepository {
fun Application.getDatabaseConf(type: RatingDbType): IRatingRepository {

    val dbSettingPath = "${ConfigPaths.repository}.${type.confName}"
    val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()

    return when (dbSetting) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
        "postgres", "postgresql", "pg", "sql", "psql" -> initPostgres()
        "cassandra", "nosql", "cass" -> initCassandra()
        "arcade", "arcadedb", "graphdb", "gremlin", "g", "a" -> initGremliln()
        else -> throw IllegalArgumentException(
            "$dbSettingPath must be set in application.yml to one of: " +
                    "'inmemory', 'postgres', 'cassandra', 'gremlin'"
        )
    }
}

private fun Application.initPostgres(): IRatingRepository {
    val config = PostgresConfig(environment.config)
    return RepoRatingSQL(
        properties = SqlProperties(
            url = config.url,
            user = config.user,
            password = config.password,
            schema = config.schema,
        )
    )
}

private fun Application.initCassandra(): IRatingRepository {
    val config = CassandraConfig(environment.config)
    return RepoRatingCassandra(
        keyspaceName = config.keyspace,
        host = config.host,
        port = config.port,
        user = config.user,
        pass = config.pass,
    )
}

private fun Application.initGremliln(): IRatingRepository {
    val config = GremlinConfig(environment.config)
    return RatingRepoGremlin(
        hosts = config.host,
        port = config.port,
        user = config.user,
        pass = config.pass,
        enableSsl = config.enableSsl,
    )
}

private fun Application.initInMemory(): IRatingRepository {
    val ttlSetting = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return RatingRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}

enum class RatingDbType(val confName: String) {
    PROD("prod"), TEST("test")
}