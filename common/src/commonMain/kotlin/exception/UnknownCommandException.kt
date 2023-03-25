package com.crowdproj.rating.common.exception

import com.crowdproj.rating.common.model.CwpRatingCommand

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 18:29
 */

class UnknownCommandException(cmd: CwpRatingCommand): RuntimeException("Command $cmd can't be mapped and not supported.")