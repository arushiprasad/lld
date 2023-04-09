package concurrency.readwritelock

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

//multiple people can read
// only one write

// while writing, both read and write should be blocked
// while reading, ensure no writer

// keep count of readers
class ReadWriteLock {

    // the functions are read lock acquire and read lock release,
    // this is about getting the permission to read, not the read itself

    // can give permission to read, as long as no write is executing

    var readers=0
    var writer=false
    val lock=ReentrantLock()
    val condition=lock.newCondition()

    fun getReadPermit(threadName:String){
        lock.withLock {
            // await means wait until called
            // we dont need to notify threads blocked to enter this block
            // we need to notify threads who are in the wait queue
            while(writer) condition.await()
            println("$threadName got reader")
            readers++
        }
    }

    fun releaseReadPermit(threadName:String){
        lock.withLock {
            readers--
            // here we should signal the writer
            condition.signal()

            println("$threadName left reader")
        }
    }

    fun getWritePermit(threadName:String){
        lock.withLock {
            while(readers!=0||writer) condition.await()
            writer=true
            println("$threadName got writer")
        }
    }

    fun releaseWritePermit(threadName:String){
        lock.withLock {
            writer=false
            // signal the reader
            condition.signal()
            println("$threadName left writer")
        }
    }
}

