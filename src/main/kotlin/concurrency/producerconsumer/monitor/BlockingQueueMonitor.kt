package concurrency.producerconsumer.monitor

import java.util.LinkedList
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * This is implementing blocking queue using Re-entrant lock
 * */
class BlockingQueueMonitor<T>(
    private val capacity: Int,
) {
    private val queue = LinkedList<T>()


    // This is similar to implicit monitor lock that comes with synchronised
    // TODO: why is it called reentrant?
    private val lock = ReentrantLock()

    // used to setIfNotExists conditions like wait and notify on lock
    private val condition = lock.newCondition()

    @Synchronized
    fun enqueue(item: T, threadName: String) {
        lock.withLock {
            while (queue.size == capacity) {
                condition.await()
            }
            println("$threadName has the monitor and adds $item")
            queue.add(item)
            condition.signalAll()
        }
    }

    fun dequeue(threadName: String): T {
        lock.withLock {
            while (queue.size == 0) {
                condition.await()
            }
            val item = queue.remove()
            println("$threadName has the monitor and gets $item")
            condition.signalAll()
            return item
        }
    }
}