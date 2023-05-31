package com.crowdproj.rating.repo.gremlin.mapper

import com.crowdproj.rating.common.NONE
import com.crowdproj.rating.common.model.*
import com.crowdproj.rating.repo.gremlin.RatingGremlinConst.FIELD_CREATED_AT
import com.crowdproj.rating.repo.gremlin.RatingGremlinConst.FIELD_ID
import com.crowdproj.rating.repo.gremlin.RatingGremlinConst.FIELD_LOCK
import com.crowdproj.rating.repo.gremlin.RatingGremlinConst.FIELD_OBJECT_ID
import com.crowdproj.rating.repo.gremlin.RatingGremlinConst.FIELD_OBJECT_TYPE_ID
import com.crowdproj.rating.repo.gremlin.RatingGremlinConst.FIELD_OWNER_ID
import com.crowdproj.rating.repo.gremlin.RatingGremlinConst.FIELD_SCORE
import com.crowdproj.rating.repo.gremlin.RatingGremlinConst.FIELD_SCORE_TYPE_ID
import com.crowdproj.rating.repo.gremlin.RatingGremlinConst.FIELD_TMP_RESULT
import com.crowdproj.rating.repo.gremlin.RatingGremlinConst.FIELD_UPDATED_AT
import com.crowdproj.rating.repo.gremlin.RatingGremlinConst.FIELD_VOTE_COUNT
import com.crowdproj.rating.repo.gremlin.RatingGremlinConst.RESULT_SUCCESS
import kotlinx.datetime.Instant
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.`__` as gr
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.apache.tinkerpop.gremlin.structure.VertexProperty

fun GraphTraversal<Vertex, Vertex>.addCwpRating(rating: CwpRating): GraphTraversal<Vertex, Vertex> =
    this
        .property(VertexProperty.Cardinality.single, FIELD_ID, rating.id.takeIf { it != CwpRatingId.NONE }?.asString())
        .property(VertexProperty.Cardinality.single, FIELD_SCORE_TYPE_ID, rating.scoreTypeId.takeIf { it != CwpRatingScoreTypeId.NONE }?.asString())
        .property(VertexProperty.Cardinality.single, FIELD_OBJECT_TYPE_ID, rating.objectTypeId.takeIf { it != CwpRatingObjectTypeId.NONE }?.asString())
        .property(VertexProperty.Cardinality.single, FIELD_OBJECT_ID, rating.objectId.takeIf { it != CwpRatingObjectId.NONE }?.asString())
        .property(VertexProperty.Cardinality.single, FIELD_SCORE, rating.score.toDouble())
        .property(VertexProperty.Cardinality.single, FIELD_VOTE_COUNT, rating.voteCount.toInt())
        .property(VertexProperty.Cardinality.single, FIELD_CREATED_AT, rating.createdAt.toString())
        .property(VertexProperty.Cardinality.single, FIELD_UPDATED_AT, rating.updatedAt.toString())
        .property(VertexProperty.Cardinality.single, FIELD_OWNER_ID, rating.ownerId.asString().takeIf { it.isNotBlank() }) // здесь можно сделать ссылку на объект владельца
        .property(VertexProperty.Cardinality.single, FIELD_LOCK, rating.lock.takeIf { it != CwpRatingLock.NONE }?.asString())

fun GraphTraversal<Vertex, Vertex>.listCwpRating(result: String = RESULT_SUCCESS): GraphTraversal<Vertex, MutableMap<String, Any>> =
    project<Any?>(
        FIELD_ID,
        FIELD_SCORE_TYPE_ID,
        FIELD_OBJECT_TYPE_ID,
        FIELD_OBJECT_ID,
        FIELD_SCORE,
        FIELD_VOTE_COUNT,
        FIELD_CREATED_AT,
        FIELD_UPDATED_AT,
        FIELD_OWNER_ID,
        FIELD_LOCK,
        FIELD_TMP_RESULT,
    )
        .by(gr.id<Vertex>()) // генерируется id
        .by(FIELD_SCORE_TYPE_ID)
        .by(FIELD_OBJECT_TYPE_ID)
        .by(FIELD_OBJECT_ID)
        .by(FIELD_SCORE)
        .by(FIELD_VOTE_COUNT)
        .by(FIELD_CREATED_AT)
        .by(FIELD_UPDATED_AT)
        .by(FIELD_OWNER_ID)
//        .by(gr.inE("Owns").outV().id())
        .by(FIELD_LOCK)
        .by(gr.constant(result)) // прокидывается результат извне (см. аргументы функции)
//        .by(elementMap<Vertex, Map<Any?, Any?>>())

fun Map<String, Any?>.toCwpRating(): CwpRating = CwpRating(
    id = (this[FIELD_ID] as? String)?.let { CwpRatingId(it) } ?: CwpRatingId.NONE,
    scoreTypeId = (this[FIELD_SCORE_TYPE_ID] as? String)?.let { CwpRatingScoreTypeId(it)} ?: CwpRatingScoreTypeId.NONE,
    objectTypeId = (this[FIELD_OBJECT_TYPE_ID] as? String)?.let { CwpRatingObjectTypeId(it)} ?: CwpRatingObjectTypeId.NONE,
    objectId = (this[FIELD_OBJECT_ID] as? String)?.let { CwpRatingObjectId(it)} ?: CwpRatingObjectId.NONE,
    score = (this[FIELD_SCORE] as? Double) ?: 0.0,
    voteCount = (this[FIELD_VOTE_COUNT] as? Int) ?: 0,
    createdAt = (this[FIELD_CREATED_AT] as? Instant) ?: Instant.NONE,
    updatedAt = (this[FIELD_UPDATED_AT] as? Instant) ?: Instant.NONE,
    ownerId = (this[FIELD_OWNER_ID] as? String)?.let { CwpRatingUserId(it) } ?: CwpRatingUserId.NONE,
    lock = (this[FIELD_LOCK] as? String)?.let { CwpRatingLock(it) } ?: CwpRatingLock.NONE,
)