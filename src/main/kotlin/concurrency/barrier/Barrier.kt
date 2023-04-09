package concurrency.barrier

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

//All or some threads need to reach before allowing to proceed further
// last thread to reach signals?

// just make everything await till condition becomes true
// last thread would update condition

// one function to add to list
// one to update barrier
class Barrier(private val numberThreads: Int) {

    private var currNum=0
    private val lock=ReentrantLock()
    private val condition=lock.newCondition()

    fun await(threadName:String){
        lock.withLock {
            currNum++
            if(currNum==numberThreads){
                condition.signalAll()
                println("hurray! $threadName release barrier")
                currNum=0
            }
            else{
                println("$threadName is waiting :(")
                condition.await()
            }
        }
    }
}