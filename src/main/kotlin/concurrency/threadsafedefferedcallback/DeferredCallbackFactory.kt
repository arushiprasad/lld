package concurrency.threadsafedefferedcallback

import java.time.Instant
import java.util.PriorityQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread
import kotlin.concurrent.withLock

// insert new entry into queue, check min time of top and then sleep till then

class DeferredCallBackFactory {
    companion object {
        fun createDeferredCallBack(): AbstractDeferredClass {
            val deferredCallback = DeferredCallback()
            // created a thread instead of running it in the main loop because it would
            // never exit
            thread(start = true){
                deferredCallback.start()
            }
            return deferredCallback
        }
    }

    private class DeferredCallback : AbstractDeferredClass {

        private val comparator = Comparator<CallBack> { o1, o2 ->
            (o1.runAfter.toEpochMilli() - o2.runAfter.toEpochMilli()).toInt()
        }
        private val queue = PriorityQueue(comparator)
        private val lock = ReentrantLock()
        private val newAddition = lock.newCondition()

        override fun enqueue(callBack: CallBack) {
            lock.withLock {
                queue.add(callBack)
                println("Adding callback")
                // wake up thread
                newAddition.signal()
            }
        }

        fun start() {
            while (true) {
                lock.withLock {
                    while (queue.size == 0) {
                        newAddition.await()
                    }

                    while(queue.size!=0){
                        val minTime =
                            queue.peek().runAfter.toEpochMilli() - Instant.now().toEpochMilli()
                        if(minTime<=0) break

                        newAddition.await(minTime, TimeUnit.MILLISECONDS)
                    }
                        val callback = queue.poll()
                        println(callback.toPrint)
                        println("Callback to be printed at ${callback.runAfter} and actually printed at${Instant.now()}")
                }
            }
        }
    }
}