package com.crowdproj.rating.repo.cassandra

import com.crowdproj.rating.common.model.CwpRatingObjectId
import com.crowdproj.rating.common.model.CwpRatingObjectTypeId
import com.crowdproj.rating.common.model.CwpRatingScoreTypeId
import com.crowdproj.rating.common.model.CwpRatingUserId
import com.crowdproj.rating.common.repo.DbRatingFilterRequest
import com.crowdproj.rating.repo.cassandra.model.RatingCassandraDTO
import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.MapperContext
import com.datastax.oss.driver.api.mapper.entity.EntityHelper
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.function.BiConsumer

class RatingCassandraSearchProvider(
    private val context: MapperContext,
    private val entityHelper: EntityHelper<RatingCassandraDTO>,
) {

    fun search(filter: DbRatingFilterRequest): CompletionStage<Collection<RatingCassandraDTO>> {
        var select = entityHelper.selectStart().allowFiltering()

        if (filter.scoreTypeId != CwpRatingScoreTypeId.NONE) {
            select = select
                .whereColumn(RatingCassandraDTO.COLUMN_SCORE_TYPE_ID)
                .isEqualTo(QueryBuilder.literal(filter.scoreTypeId.asString(), context.session.context.codecRegistry))
        }
        if (filter.objectTypeId != CwpRatingObjectTypeId.NONE) {
            select = select
                .whereColumn(RatingCassandraDTO.COLUMN_OBJECT_TYPE_ID)
                .isEqualTo(QueryBuilder.literal(filter.objectTypeId.asString(), context.session.context.codecRegistry))
        }
        if (filter.objectId != CwpRatingObjectId.NONE) {
            select = select
                .whereColumn(RatingCassandraDTO.COLUMN_OBJECT_ID)
                .isEqualTo(QueryBuilder.literal(filter.objectId.asString(), context.session.context.codecRegistry))
        }
        if (filter.ownerId != CwpRatingUserId.NONE) {
            select = select
                .whereColumn(RatingCassandraDTO.COLUMN_OWNER_ID)
                .isEqualTo(QueryBuilder.literal(filter.ownerId.asString(), context.session.context.codecRegistry))
        }

        val asyncFetcher = AsyncFetcher()

        context.session
            .executeAsync(select.build())
            .whenComplete(asyncFetcher)

        return asyncFetcher.stage
    }

    inner class AsyncFetcher : BiConsumer<AsyncResultSet?, Throwable?> {
        private val buffer = mutableListOf<RatingCassandraDTO>()
        private val future = CompletableFuture<Collection<RatingCassandraDTO>>()
        val stage: CompletionStage<Collection<RatingCassandraDTO>> = future

        override fun accept(resultSet: AsyncResultSet?, t: Throwable?) {
            when {
                t != null -> future.completeExceptionally(t)
                resultSet == null -> future.completeExceptionally(IllegalStateException("ResultSet should not be null"))
                else -> {
                    buffer.addAll(resultSet.currentPage().map { entityHelper.get(it, false) })
                    if (resultSet.hasMorePages())
                        resultSet.fetchNextPage().whenComplete(this)
                    else
                        future.complete(buffer)
                }
            }
        }
    }
}
