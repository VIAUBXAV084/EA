package hu.bme.kszk.service.rooms.blocked

import hu.bme.kszk.db.rooms.BlockedRoomsEntity
import hu.bme.kszk.dto.rooms.BlockedRoomRequest
import java.util.*

interface BlockedRoomsServiceInterface {
    fun getAllBlocked() : List<BlockedRoomsEntity>
    fun getBlockedRoomById(uuid: String) : BlockedRoomsEntity?
    fun getBlockedRoomById(uuid: UUID): BlockedRoomsEntity?
    fun createBlockedRoom(createBlockedRoomRequest: BlockedRoomRequest) : BlockedRoomsEntity
    fun updateBlockedRoom(updateBlockedRoomRequest: BlockedRoomRequest): BlockedRoomsEntity?
    fun deleteBlockedRoom(uuid: String) : Unit
}