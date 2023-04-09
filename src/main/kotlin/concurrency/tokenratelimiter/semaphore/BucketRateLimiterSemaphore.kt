package concurrency.tokenratelimiter.semaphore

import java.lang.Thread.sleep
import java.util.concurrent.Semaphore
import kotlin.concurrent.thread

class BucketRateLimiterSemaphore(
    val capacity:Int
) {

    var size=0
    val semaphore=Semaphore(0)
    val lock=Semaphore(1)

    init{
        thread(start=true){
            while(true){
                lock.acquire()
                if(size<capacity){
                    size++
                    semaphore.release()
                    println("Added token")
                }else{
                    println("Full!")
                }
                lock.release()
                sleep(1000)
            }

        }
    }

    fun dequeue(threadName:String){
        semaphore.acquire()
        lock.acquire()

        size--
        println("$threadName acquired token")

        lock.release()
    }
}