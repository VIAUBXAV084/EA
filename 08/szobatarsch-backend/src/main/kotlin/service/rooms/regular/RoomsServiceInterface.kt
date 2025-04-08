package hu.bme.kszk.service.rooms.regular

import hu.bme.kszk.db.rooms.RoomsEntity
import hu.bme.kszk.dto.rooms.RoomRequest
import java.util.*

interface RoomsServiceInterface {
    fun getAllRooms() : List<RoomsEntity>
    fun getRoomById(uuid: String) : RoomsEntity?
    fun getRoomById(uuid: UUID): RoomsEntity?
    fun getRoomByUserId(uuid: String?): RoomsEntity?
    fun createRoom(createRoomRequest: RoomRequest) : RoomsEntity
    fun updateRoom(updateRoomRequest: RoomRequest) : RoomsEntity?
    fun deleteRoom(uuid: String) : Unit
}