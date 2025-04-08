package hu.bme.kszk.service.rooms.blocked

import hu.bme.kszk.db.rooms.BlockedRoomsEntity
import hu.bme.kszk.dto.rooms.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class BlockedRoomsService : BlockedRoomsServiceInterface {
    override fun getAllBlocked(): List<BlockedRoomsEntity> = transaction { BlockedRoomsEntity.all().toList() }
    override fun getBlockedRoomById(uuid: String): BlockedRoomsEntity? = transaction { BlockedRoomsEntity.findById(UUID.fromString(uuid)) }
    override fun getBlockedRoomById(uuid: UUID): BlockedRoomsEntity? = transaction { BlockedRoomsEntity.findById(uuid) }
    override fun createBlockedRoom(createBlockedRoomRequest: BlockedRoomRequest): BlockedRoomsEntity = transaction { createBlockedRoomRequest.toBlockedRoomEntity() }
    override fun updateBlockedRoom(updateBlockedRoomRequest: BlockedRoomRequest): BlockedRoomsEntity? = transaction { getBlockedRoomById(updateBlockedRoomRequest.roomUUid)?.apply { updateFromBlockedRoomRequest(updateBlockedRoomRequest) } }
    override fun deleteBlockedRoom(uuid: String): Unit = transaction { getBlockedRoomById(uuid)?.delete() }
}