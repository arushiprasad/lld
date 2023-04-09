package concurrency.semaphoreusingmutex

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class SemaphoreImpl(
    private val initialSize: Int? = 0
) {

    private var size = initialSize ?: 0
    private val lock = ReentrantLock()
    private val condition = lock.newCondition()

    // I know this looks weird here, but thats because this is internal working.
    // When actually applied, think of using lock vs this impl of semaphore somewhere.

    // if we do lock, then no thread can enter while lock is not unlocked. but here
    // can call semaphore.acquire upto 5 times before releasing, so 5 threads can access it

    fun acquire(threadName: String) {
        lock.withLock {
            while (size < 1) {
                println("sorry please try again later $threadName")
                condition.await()
            }

            size--
            println("$threadName acquired semaphore")
        }
    }

    fun release(threadName: String) {
        lock.withLock {
            size++
            println("$threadName released semaphore")
            condition.signalAll()
        }
    }
}