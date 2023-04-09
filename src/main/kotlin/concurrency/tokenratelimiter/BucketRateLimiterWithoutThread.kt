package concurrency.tokenratelimiter

import java.lang.Thread.sleep
import java.time.Instant
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class BucketRateLimiterWithoutThread(
    private val maxToken: Int
) {

    private val lock = ReentrantLock()

    private var lastRequest: Instant = Instant.now()
    private var availableTokenCount = 0

    fun dequeue(threadName: String) {
        lock.withLock {
            val timePassed = (Instant.now().toEpochMilli() - lastRequest.toEpochMilli()) / 1000

            if (timePassed >= maxToken) {
                availableTokenCount = maxToken
            } else {
                availableTokenCount += timePassed.toInt()
            }

            // sleep should release the lock
            if (availableTokenCount < 1) {
                println("$threadName going to sleep while waiting for token")
                sleep(1000)
            } else availableTokenCount--

            println("$threadName got token")

            lastRequest = Instant.now()
        }
    }
}