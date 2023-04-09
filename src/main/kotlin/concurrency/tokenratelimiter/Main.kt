package concurrency.tokenratelimiter

import concurrency.tokenratelimiter.monitor.BucketRateLimiterMonitor
import concurrency.tokenratelimiter.semaphore.BucketRateLimiterSemaphore
import java.lang.Thread.sleep
import kotlin.concurrent.thread

fun main() {
    //bucketRateLimiterWithReentrantLock()
    //bucketRateLimiterWithSemaphore()
    bucketRateLimiterWithoutThread()
}

fun bucketRateLimiterWithReentrantLock() {
    val ratelimiter = BucketRateLimiterMonitor(5)

    val threadA = thread(start = true, isDaemon = true) {
        for (i in 1..1000) {
            ratelimiter.dequeue("threadA")
            sleep(6000)
        }
    }

    val threadB = thread(start = true, isDaemon = true) {
        for (i in 1..1000) {
            ratelimiter.dequeue("threadB")
            sleep(6000)
        }
    }

    val threadC = thread(start = true, isDaemon = true) {
        for (i in 1..1000) {
            ratelimiter.dequeue("threadC")
            sleep(6000)
        }
    }
}

fun bucketRateLimiterWithSemaphore() {
    val ratelimiter = BucketRateLimiterSemaphore(5)

    val threadA = thread(start = true, isDaemon = true) {
        for (i in 1..1000) {
            ratelimiter.dequeue("threadA")
            sleep(6000)
        }
    }

    val threadB = thread(start = true, isDaemon = true) {
        for (i in 1..1000) {
            ratelimiter.dequeue("threadB")
            sleep(6000)
        }
    }

    val threadC = thread(start = true, isDaemon = true) {
        for (i in 1..1000) {
            ratelimiter.dequeue("threadC")
            sleep(6000)
        }
    }
}

fun bucketRateLimiterWithoutThread() {
    val ratelimiter = BucketRateLimiterWithoutThread(5)

    val threadA = thread(start = true, isDaemon = true) {
        for (i in 1..1000) {
            ratelimiter.dequeue("threadA")
            sleep(3000)
        }
    }

    val threadB = thread(start = true, isDaemon = true) {
        for (i in 1..1000) {
            ratelimiter.dequeue("threadB")
            sleep(3000)
        }
    }

    val threadC = thread(start = true, isDaemon = true) {
        for (i in 1..1000) {
            ratelimiter.dequeue("threadC")
            sleep(4000)
        }
    }

    threadA.join()
    sleep(5000)
}