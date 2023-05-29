package com.crowdproj.rating.repo.cassandra

import com.crowdproj.rating.common.repo.DbRatingFilterRequest
import com.crowdproj.rating.repo.cassandra.model.RatingCassandraDTO
import com.datastax.oss.driver.api.mapper.annotations.*
import java.util.concurrent.CompletionStage

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  18.05.2023 15:38
 */

@Dao
interface RatingCassandraDAO {

    @Insert
    fun create(dto: RatingCassandraDTO): CompletionStage<Unit>

    @Select
    fun read(id: String): CompletionStage<RatingCassandraDTO?>

    @Update(customIfClause = "lock = :prevLock") // если не равно, то update свалится
    fun update(dto: RatingCassandraDTO, prevLock: String): CompletionStage<Boolean>

    @Delete(customWhereClause = "id = :id", customIfClause = "lock = :prevLock", entityClass = [RatingCassandraDTO::class])
    fun delete(id: String, prevLock: String): CompletionStage<Boolean>

    @QueryProvider(providerClass = RatingCassandraSearchProvider::class, entityHelpers = [RatingCassandraDTO::class])
    fun search(filter: DbRatingFilterRequest): CompletionStage<Collection<RatingCassandraDTO>>
}