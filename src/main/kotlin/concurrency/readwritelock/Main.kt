package concurrency.readwritelock

import java.lang.Thread.sleep
import kotlin.concurrent.thread

fun main(){
    val readWriteLock=ReadWriteLock()

    val threadA= thread {
        while(true){
            sleep(100)
            readWriteLock.getWritePermit("A")
            sleep(10000)
            readWriteLock.releaseWritePermit("A")
            sleep(1000)
        }

    }

    val threadB= thread {
        while(true){
            readWriteLock.getReadPermit("B")
            readWriteLock.getReadPermit("B")
            sleep(1000)
            readWriteLock.releaseReadPermit("B")
            sleep(900)
            readWriteLock.releaseReadPermit("B")
            sleep(100)
        }
    }
}