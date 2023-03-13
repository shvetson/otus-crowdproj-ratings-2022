package exception

import models.MkplCommand

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 18:29
 */

class UnknownCommandException(cmd: MkplCommand): RuntimeException("Command $cmd can't be mapped and not supported.")