package parkinglot.repository.impl

import parkinglot.api.Lot
import parkinglot.api.LotStatus
import parkinglot.api.ParkingLot
import parkinglot.api.Vehicle
import parkinglot.api.VehicleType
import parkinglot.repository.ParkingRepository
import java.util.TreeMap

class ParkingRepositoryImpl() : ParkingRepository {

    private val carlots= TreeMap<Int, Lot>()
    private val trucklots= TreeMap<Int, Lot>()
    private val bikelots= TreeMap<Int, Lot>()


    override fun createLots(parkingLot: ParkingLot): Int {
       parkingLot.lots.forEach {
           when(it.allowedType){
               listOf(VehicleType.car)-> carlots[it.id] = it
               listOf(VehicleType.truck)-> trucklots[it.id] = it
               listOf(VehicleType.bike)-> bikelots[it.id] = it
           }
       }
        return parkingLot.id
    }

    override fun findAvailableLot(vehicle: Vehicle): Int? {
        return when(vehicle.type){
            VehicleType.car->carlots.entries.firstOrNull {
                it.value.status==LotStatus.free
            }?.value?.id

            VehicleType.bike -> bikelots.entries.firstOrNull {
                it.value.status==LotStatus.free
            }?.value?.id
            VehicleType.truck -> trucklots.entries.firstOrNull {
                it.value.status==LotStatus.free
            }?.value?.id
            VehicleType.bus -> {
                // Do nothing, let the previous layer throw error
                -1
            }
        }

    }

    override fun updateLot(lot: Int, status: LotStatus): Boolean {
        if(carlots.containsKey(lot)){
            carlots[lot]=carlots[lot]!!.copy(status = status)
            return true
        }
        else if(bikelots.containsKey(lot)){
            bikelots[lot]=bikelots[lot]!!.copy(status = status)
            return true
        }
        else if(trucklots.containsKey(lot)){
            trucklots[lot]=trucklots[lot]!!.copy(status = status)
            return true
        }
        return false
    }

    override fun findLotSummary(lotStatus: LotStatus, vehicleType: VehicleType?) {
        TODO("Not yet implemented")
    }
}