package parkinglot.api

import java.time.Instant

data class ParkingLot(
    val id:Int,
    val lots:List<Lot>,
    val floors:Int,
    val metaData:ParkingLotMetaData?
)

data class ParkingLotMetaData (
    val name:String,
    val city:String
)

data class Lot(
    val id:Int,
    val floor:Int,
    val allowedType:List<VehicleType>,
    val status:LotStatus,
    val vehicleId:Int?
)

data class ParkingLotCreationPayload(
    // each floor has a list of lots with information about what is allowed in them
    val floors:List<List<VehicleType>>
)

enum class LotStatus{
    free,
    occupied
}

data class Ticket(
    val id:Int,
    val vehicleId: Int,
    val lotId:Int,
    val timeOfEntry:Instant,
    val timeOfExit:Instant?,
    val status:TicketStatus,
    val amountDue:Int=0
    // can take in a calculator, that decides how much should be paid
)

enum class TicketStatus{
    paid,
    unpaid
}