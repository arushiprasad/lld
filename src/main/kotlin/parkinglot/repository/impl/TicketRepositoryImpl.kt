package parkinglot.repository.impl

import parkinglot.api.Ticket
import parkinglot.api.TicketStatus
import parkinglot.repository.TicketRepository
import java.time.Instant
import kotlin.random.Random

class TicketRepositoryImpl:TicketRepository {
    private val tickets= HashMap<Int, Ticket>()
    override fun createTicket(vehicleId: Int, lotId: Int): Int {
        val id=getId()
        tickets[id] = Ticket(
            id = id,
            vehicleId = vehicleId,
            lotId = lotId,
            timeOfEntry = Instant.now(),
            status = TicketStatus.unpaid,
            timeOfExit = null
        )
        return id;
    }

    override fun getTicket(ticketId: Int): Ticket? {
        return tickets[ticketId]
    }

    override fun updateTicket(ticket: Ticket): Boolean {
        tickets[ticket.id]=ticket
        return true
    }

    override fun deleteTicket(ticket: Ticket): Boolean {
        tickets.remove(ticket.id)
        return true
    }

    private fun getId():Int{
        return Random.nextInt()
    }
}