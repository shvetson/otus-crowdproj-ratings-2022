package com.crowdproj.rating.repo.gremlin

object RatingGremlinConst {
    const val RESULT_SUCCESS = "success"
    const val RESULT_LOCK_FAILURE = "lock-failure"

    const val FIELD_ID = "#id"
    const val FIELD_SCORE_TYPE_ID = "scoreTypeId"
    const val FIELD_OBJECT_TYPE_ID = "objectTypeId"
    const val FIELD_OBJECT_ID = "objectId"
    const val FIELD_SCORE = "score"
    const val FIELD_VOTE_COUNT = "voteCount"
    const val FIELD_CREATED_AT = "createdAt"
    const val FIELD_UPDATED_AT = "updatedAt"
    const val FIELD_OWNER_ID = "ownerId"
    const val FIELD_LOCK = "lock"
    const val FIELD_TMP_RESULT = "_result"
}