package com.crowdproj.rating.common.exception

import kotlin.reflect.KClass

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  21.02.2023 17:01
 */

class UnknownRequestException(cls: KClass<*>): RuntimeException("Class $cls can't be mapped and not supported.")