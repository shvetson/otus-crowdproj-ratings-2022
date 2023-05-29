package com.crowdproj.rating.repo.cassandra

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace
import com.datastax.oss.driver.api.mapper.annotations.DaoTable
import com.datastax.oss.driver.api.mapper.annotations.Mapper

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  18.05.2023 16:21
 */

@Mapper
interface CassandraMapper {

    @DaoFactory
    fun ratingDao(@DaoKeyspace keyspace: String, @DaoTable tableName: String): RatingCassandraDAO

    companion object {
        fun builder(session: CqlSession) = CassandraMapperBuilder(session)
    }
}