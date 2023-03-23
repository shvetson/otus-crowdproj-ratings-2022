package com.crowdproj.rating.common.exception

import com.crowdproj.rating.common.model.CwpRatCommand

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 18:29
 */

class UnknownCommandException(cmd: CwpRatCommand): RuntimeException("Command $cmd can't be mapped and not supported.")