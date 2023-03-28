package parkinglot.api

import java.time.Instant

data class Ticket(
    val id:Int,
    val vehicleId: Int,
    val lotId:Int,
    val timeOfEntry: Instant,
    val timeOfExit: Instant?,
    val status:TicketStatus,
    val amountDue:Int=0
    // can take in a calculator, that decides how much should be paid
)

enum class TicketStatus{
    paid,
    unpaid
}