package parkinglot.service

import parkinglot.api.Lot
import parkinglot.api.ParkingLotCreationPayload
import parkinglot.api.LotStatus
import parkinglot.api.ParkingLot
import parkinglot.api.Ticket
import parkinglot.api.Vehicle
import parkinglot.repository.ParkingRepository
import parkinglot.repository.TicketRepository
import parkinglot.repository.impl.ParkingRepositoryImpl
import parkinglot.repository.impl.TicketRepositoryImpl
import java.lang.Exception
import java.time.Instant

// Singleton
object ParkingLotService {

    private var PARKING_LOT_ID: Int? = null
    private lateinit var parkingRepository: ParkingRepository
    private lateinit var ticketRepository: TicketRepository
    private var incrementalId = 1;

    fun createParkingLot(parkingLotCreationPayload: ParkingLotCreationPayload): Int {
        return PARKING_LOT_ID ?: synchronized(this) {
            PARKING_LOT_ID ?: initParkingLot(
                parkingLotCreationPayload
            )
        }
    }

    /** Parks vehicle into [Lot] and returns [Ticket] number */
    @Synchronized
    fun parkVehicle(vehicle: Vehicle): Int {
        val lotId =
            parkingRepository.findAvailableLot(vehicle) ?: throw Exception("No available lots")
        parkingRepository.updateLot(lotId, LotStatus.occupied)
        return ticketRepository.createTicket(vehicle.id, lotId)
    }

    /** Unparks vehicle from [Lot] and returns updated [Ticket] number with bill*/
    @Synchronized
    fun unparkVehicle(ticketId: Int): Ticket {
        parkingRepository.updateLot(ticketId, LotStatus.occupied)
        val ticket = ticketRepository.getTicket(ticketId)
        ticket ?: throw NoSuchElementException("No ticket exists")
        //TODO: needs to be able to find cost
        ticketRepository.updateTicket(ticket.copy(timeOfExit = Instant.now(), amountDue = 10))
        return ticketRepository.getTicket(ticketId)!!
    }

    fun getLotSummary(lotStatus: LotStatus, floor: Int?) {
        TODO()
    }

    private fun initParkingLot(parkingLotCreationPayload: ParkingLotCreationPayload): Int {
        parkingRepository = ParkingRepositoryImpl()
        val parkingLotId =
            parkingRepository.createLots(getParkingLot(parkingLotCreationPayload))
        ticketRepository = TicketRepositoryImpl()

        return parkingLotId
    }

    private fun getParkingLot(parkingLotCreationPayload: ParkingLotCreationPayload): ParkingLot {

        val lots = mutableListOf<Lot>()
        for ((floor, i) in parkingLotCreationPayload.floors.withIndex()) {
            i.forEach {
                lots.add(
                    Lot(
                        id = incrementalId++,
                        allowedType = listOf(it),
                        floor = floor,
                        status = LotStatus.free,
                        vehicleId = null
                    )
                )
            }
        }

        return ParkingLot(1, lots, parkingLotCreationPayload.floors.size, null)
    }
}
