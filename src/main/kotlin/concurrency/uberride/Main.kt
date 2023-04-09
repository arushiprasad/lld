package concurrency.uberride

import java.lang.Thread.sleep
import kotlin.concurrent.thread

fun main(){
    val uber=UberRideSemaphore()

    thread(start = true) {
        uber.requestRide(RiderType.republican,"A")
    }

    thread(start = true) {
        sleep(200)
        uber.requestRide(RiderType.democrat,"B")
    }

    thread(start = true) {
        sleep(300)
        uber.requestRide(RiderType.republican,"C")
    }

    thread(start = true) {
        sleep(400)
        uber.requestRide(RiderType.republican,"D")
    }

    thread(start = true) {
        sleep(500)
        uber.requestRide(RiderType.republican,"E")
    }

    thread(start = true) {
        sleep(100)
        uber.requestRide(RiderType.democrat,"F")
    }

    thread(start = true) {
        sleep(500)
        uber.requestRide(RiderType.democrat,"G")
    }

    thread(start = true) {
        sleep(600)
        uber.requestRide(RiderType.democrat,"H")
    }

    thread(start = true) {
        sleep(500)
        uber.requestRide(RiderType.democrat,"Ee")
    }

    thread(start = true) {
        sleep(100)
        uber.requestRide(RiderType.democrat,"Fe")
    }

    thread(start = true) {
        sleep(500)
        uber.requestRide(RiderType.democrat,"Ge")
    }

    thread(start = true) {
        sleep(600)
        uber.requestRide(RiderType.democrat,"He")
    }
}