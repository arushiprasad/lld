package parkinglot.api

data class Vehicle (
    val id:Int,
    val metaData:VehicleMetadata,
    val type:VehicleType
)

data class VehicleMetadata(
    val colour:String,
    val company:String
)


enum class VehicleType {
    car,
    bike,
    truck,
    bus
}