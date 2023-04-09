package concurrency.producerconsumer

import concurrency.producerconsumer.monitor.BlockingQueueMonitor
import concurrency.producerconsumer.semaphore.BlockingQueueSemaphore
import java.lang.Thread.sleep
import kotlin.concurrent.thread

fun main(){

    //monitorQueue()
    semaphoreQueue()
}

fun monitorQueue(){
    val synchronizedBlockingQueue=BlockingQueueMonitor<Int>(5)

    //PRODUCER

    // what does daemon do?
    val threadA=thread(start = true, isDaemon = true){
        for (i in 1..100){
            synchronizedBlockingQueue.enqueue(i,"threadA")
        }
    }

    // CONSUMER
    val threadB=thread(start = true, isDaemon = true){
        for (i in 1..50){
            synchronizedBlockingQueue.dequeue("threadB")
        }
    }

    val threadC=thread(start = true, isDaemon = true){
        for (i in 1..50){
            synchronizedBlockingQueue.dequeue("threadC")
        }
    }

    // explicitly tells parent thread to stop executing only after thread C has
    threadC.join()

    // delaying completion of main thread so can see other threads run
    sleep(5000)
}

fun semaphoreQueue(){
    val semaphoreBlockingQueue=BlockingQueueSemaphore<Int>(5)

    //PRODUCER

    // what does daemon do?
    val threadA=thread(start = true, isDaemon = true){
        for (i in 1..100){
            semaphoreBlockingQueue.enqueue(i,"threadA")
            sleep(1000)
        }
    }


    // CONSUMER
    val threadB=thread(start = true, isDaemon = true){
        for (i in 1..50){
            semaphoreBlockingQueue.dequeue("threadB")
        }
    }

    val threadC=thread(start = true, isDaemon = true){
        for (i in 1..50){
            semaphoreBlockingQueue.dequeue("threadC")
        }
    }

    // explicitly tells parent thread to stop executing only after thread C has
    threadC.join()

    // delaying completion of main thread so can see other threads run
    sleep(5000)
}