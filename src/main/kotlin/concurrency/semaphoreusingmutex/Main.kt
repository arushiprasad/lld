package concurrency.semaphoreusingmutex

import java.lang.Thread.sleep
import kotlin.concurrent.thread

fun main(){
    val semaphore=SemaphoreImpl(2)

    val threadA= thread {
        while(true){
            semaphore.acquire("threadA")
            sleep(1000)
            semaphore.release("threadA")
            sleep(8000)
        }
    }

    val threadB= thread {
        while(true){
            semaphore.acquire("threadB")
            sleep(7000)
            semaphore.release("threadB")
            sleep(1000)
        }
    }

    val threadC= thread {
        while(true){
            semaphore.acquire("threadC")
            sleep(5000)
            semaphore.release("threadC")
            sleep(3000)
        }

    }

    sleep(7000)
    threadA.join()
}