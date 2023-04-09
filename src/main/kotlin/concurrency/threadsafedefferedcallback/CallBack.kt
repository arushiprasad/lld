package concurrency.threadsafedefferedcallback

import java.time.Instant

data class CallBack (
    val toPrint:String,
    val runAfter: Instant
)