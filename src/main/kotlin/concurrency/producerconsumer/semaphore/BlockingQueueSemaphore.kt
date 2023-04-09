package concurrency.producerconsumer.semaphore

import java.util.LinkedList
import java.util.concurrent.Semaphore

class BlockingQueueSemaphore<T>(
    private val size: Int
) {

    // init to size because that's the max number of entries that can be added to queue
    // once full, queue shall give no more permits
    private val producerSemaphore = Semaphore(size)

    // starts with 0 because in the beginning queue has no items, hence shall not give permit
    // once item is added by the producer, it can release the consumer semaphore which will
    // increment the permit it has
    private val consumerSemaphore = Semaphore(0)

    // using semaphore to behave as lock here, could also use reentrant lock
    private val lock=Semaphore(1)

    private val queue=LinkedList<T>()

    fun enqueue(item:T,threadName:String){

        producerSemaphore.acquire()
        lock.acquire()

        println("$threadName adds $item")

        queue.add(item)

        lock.release()
        consumerSemaphore.release()
    }

    fun dequeue(threadName: String):T{
        consumerSemaphore.acquire()
        lock.acquire()

        val item=queue.remove()

        println("$threadName removes $item")

        lock.release()
        producerSemaphore.release()
        return item
    }
}