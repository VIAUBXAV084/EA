package hu.bme.kszk.service.rooms.regular


import hu.bme.kszk.db.rooms.RoomsEntity
import hu.bme.kszk.db.users.UsersEntity
import hu.bme.kszk.dto.rooms.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class RoomsService : RoomsServiceInterface {
    override fun getAllRooms(): List<RoomsEntity> = transaction { RoomsEntity.all().toList() }
    override fun getRoomById(uuid: String): RoomsEntity? = transaction { val room = RoomsEntity.findById(UUID.fromString(uuid)); println(room); println(RoomsEntity.all().toList().map { it.toRoomResponse() }); room }
    override fun getRoomById(uuid: UUID): RoomsEntity? = transaction { RoomsEntity.findById(uuid) }
    override fun getRoomByUserId(uuid: String?): RoomsEntity? = transaction { uuid?.let {
        UsersEntity.findById(
            UUID.fromString(
                uuid
            )
        )?.room
    } }

    override fun createRoom(createRoomRequest: RoomRequest): RoomsEntity = transaction { createRoomRequest.toRoomEntity() }
    override fun updateRoom(updateRoomRequest: RoomRequest): RoomsEntity? = transaction { getRoomById(updateRoomRequest.roomUUid)?.apply { updateFromRoomRequest(updateRoomRequest) } }
    override fun deleteRoom(uuid: String): Unit = transaction { getRoomById(uuid)?.delete() }

}