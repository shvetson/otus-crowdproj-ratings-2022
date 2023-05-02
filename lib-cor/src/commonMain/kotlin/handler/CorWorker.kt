package com.crowdproj.rating.cor.handler

import com.crowdproj.rating.cor.CorDslMarker
import com.crowdproj.rating.cor.ICorExec
import com.crowdproj.rating.cor.ICorWorkerDsl

class CorWorker<T>(
    title: String,
    description: String = "",

    blockOn: suspend T.() -> Boolean = { true },
    blockExcept: suspend T.(Throwable) -> Unit = {},

    private val blockHandle: suspend T.() -> Unit = {},
) : AbstractCorExec<T>(title, description, blockOn, blockExcept) {

    override suspend fun handle(context: T) = context.blockHandle()
}

@CorDslMarker
class CorWorkerDsl<T> : CorExecDsl<T>(), ICorWorkerDsl<T> {
    private var blockHandle: suspend T.() -> Unit = {}
    override fun handle(function: suspend T.() -> Unit) {
        blockHandle = function
    }

    override fun build(): ICorExec<T> = CorWorker(
        title = title,
        description = description,
        blockOn = blockOn,
        blockHandle = blockHandle,
        blockExcept = blockExcept
    )
}
