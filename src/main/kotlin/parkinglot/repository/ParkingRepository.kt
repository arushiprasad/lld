package parkinglot.repository

import parkinglot.api.LotStatus
import parkinglot.api.ParkingLot
import parkinglot.api.Vehicle
import parkinglot.api.VehicleType

interface ParkingRepository {

    fun createLots(parkingLot: ParkingLot):Int

    /*This should use a priority queue to find first available*/
    fun findAvailableLot(vehicle: Vehicle): Int?

    fun updateLot(lot: Int,status: LotStatus): Boolean

    fun findLotSummary(lotStatus: LotStatus,vehicleType: VehicleType?)
}