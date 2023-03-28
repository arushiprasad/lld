package parkinglot.repository

import parkinglot.api.Ticket

interface TicketRepository {

    fun createTicket(vehicleId: Int,lotId: Int): Int

    fun getTicket(ticketId:Int): Ticket?

    fun updateTicket(ticket:Ticket):Boolean

    fun deleteTicket(ticket:Ticket):Boolean
}