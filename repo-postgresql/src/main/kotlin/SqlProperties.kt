package com.crowdproj.rating.repo.postgresql

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  16.05.2023 22:51
 */

open class SqlProperties(
    val url: String = "jdbc:postgresql://localhost:5432/rating",
    val user: String = "postgres",
    val password: String = " postgres",
    val schema: String = "rating",
    val dropDatabase: Boolean = false,
//    val fastMigration: Boolean = true,
)