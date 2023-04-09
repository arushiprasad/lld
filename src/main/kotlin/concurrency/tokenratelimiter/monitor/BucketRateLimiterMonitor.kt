package concurrency.tokenratelimiter.monitor

import java.util.LinkedList
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread
import kotlin.concurrent.withLock
import kotlin.random.Random

//TODO: See why we need a factory for creating this class:
// https://github.com/the-prksh/educative-courses/blob/master/Java%20Multithreading%20for%20Senior%20Engineering%20Interviews%20-%20Learn%20Interactively/32_____continued.pdf

/**
 * TODO: Need Factory for creating this class.
 * If we create object inside constructor, the newly started thread
 * might be able to see the enclosing object **before** its full construction is complete.
 * In concurrent contexts, this may cause subtle bugs.*/
class BucketRateLimiterMonitor(
    private val maxSize:Int
) {

    val lock=ReentrantLock()
    val condition=lock.newCondition()

    val queue= LinkedList<Int>()

    init{
        thread(start=true){
            while(true){
                if(queue.size<maxSize) {
                    val item=Random.nextInt()
                    enqueue(item)
                    println("Adding token $item")
                }else{
                    println("queue is full!")
                }
                Thread.sleep(1000)
            }
        }
    }

    fun dequeue(threadName: String):Int{
        lock.withLock {
            while(queue.size==0){
                println("$threadName didn't find anything :(")
                condition.await()
            }

            val item=queue.remove()
            println("$threadName got $item")
            // will the threads try themselves? or do we need to signal? i think we need to
            //condition.signalAll()
            // NOTE: dont need to signal because the producer is doing it
            return item
        }
    }

    private fun enqueue(item:Int){
            lock.withLock {
                    queue.add(item)
                    condition.signal()
            }
    }
}