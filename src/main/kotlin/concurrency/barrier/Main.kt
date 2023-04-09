package concurrency.barrier

import java.lang.Thread.sleep
import kotlin.concurrent.thread

fun main(){
    val barrier=Barrier(3)

    thread(start = true) {
        barrier.await("A")
        barrier.await("A")
        barrier.await("A")
        barrier.await("A")
    }

    thread(start = true) {
        sleep(1000)
        barrier.await("B")
        sleep(1000)
        barrier.await("B")
        sleep(1000)
        barrier.await("B")
        sleep(1000)
        barrier.await("B")
    }

    thread(start = true) {
        sleep(1500)
        barrier.await("C")
        sleep(1500)
        barrier.await("C")
        sleep(1500)
        barrier.await("C")
        sleep(1500)
        barrier.await("C")
    }.join()


}