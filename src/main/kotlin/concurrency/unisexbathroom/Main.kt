package concurrency.unisexbathroom

import java.lang.Thread.sleep
import kotlin.concurrent.thread

fun main() {
    val unisexBathroom = UnisexBathroom(3)

    thread(start = true) {
        unisexBathroom.useBathroom(Gender.female, "A")
    }

    thread(start = true) {

        unisexBathroom.useBathroom(Gender.female, "E")

    }

    thread(start = true) {

        unisexBathroom.useBathroom(Gender.female, "B")

    }

    thread(start = true) {

        unisexBathroom.useBathroom(Gender.female, "C")

    }

    thread(start = true) {

        unisexBathroom.useBathroom(Gender.male, "D")

    }
}