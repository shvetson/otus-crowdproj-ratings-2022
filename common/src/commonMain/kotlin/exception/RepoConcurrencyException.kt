package com.crowdproj.rating.common.exception

import com.crowdproj.rating.common.model.CwpRatingLock

class RepoConcurrencyException(expectedLock: CwpRatingLock, actualLock: CwpRatingLock?): RuntimeException(
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
