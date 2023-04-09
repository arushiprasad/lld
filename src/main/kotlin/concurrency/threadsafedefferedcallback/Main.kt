package concurrency.threadsafedefferedcallback

import java.lang.Thread.sleep
import java.time.Instant
import kotlin.concurrent.thread

fun main() {

    val deferredCallBack = DeferredCallBackFactory.createDeferredCallBack()

    deferredCallBack.enqueue(
        CallBack(
            "Hello this is 1",
            Instant.now().plusMillis(8000)
        )
    )

    deferredCallBack.enqueue(
        CallBack(
            "Hello this is 2",
            Instant.now().plusMillis(9000)
        )
    )

    deferredCallBack.enqueue(
        CallBack(
            "Hello this is 3",
            Instant.now().plusMillis(5000)
        )
    )

    deferredCallBack.enqueue(
        CallBack(
            "Hello this is 4",
            Instant.now().plusMillis(2000)
        )
    )
}