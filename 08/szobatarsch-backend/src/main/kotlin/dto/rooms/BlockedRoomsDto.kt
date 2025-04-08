package hu.bme.kszk.dto.rooms

import hu.bme.kszk.db.rooms.BlockedRoomsEntity
import hu.bme.kszk.szobatarsch.enums.RoomOrientation
import kotlinx.serialization.Serializable

@Serializable
data class BlockedRoomResponse(
    val roomUUid: String,
    val roomNumber: Int,
    val roomDescription: String,
    val isCoedRoom: Boolean,
    val hasLovers: Boolean,
    val isQuiet: Boolean,
    val isSocial: Boolean,
    val orientation: String,
    val nickname: String,
    val capacity: Int
)

@Serializable
data class BlockedRoomRequest(
    val roomUUid: String = "",
    val roomNumber: Int,
    val roomDescription: String,
    val isCoedRoom: Boolean,
    val hasLovers: Boolean,
    val isQuiet: Boolean,
    val isSocial: Boolean,
    val orientation: String,
    val nickname: String,
    val capacity: Int
)


fun BlockedRoomRequest.toBlockedRoomEntity(): BlockedRoomsEntity =
    BlockedRoomsEntity.new {
        roomNumber = this@toBlockedRoomEntity.roomNumber
        roomDescription = this@toBlockedRoomEntity.roomDescription
        isCoedRoom = this@toBlockedRoomEntity.isCoedRoom
        hasLovers = this@toBlockedRoomEntity.hasLovers
        isQuiet = this@toBlockedRoomEntity.isQuiet
        isSocial = this@toBlockedRoomEntity.isSocial
        orientation = RoomOrientation.valueOf(this@toBlockedRoomEntity.orientation)
        nickname = this@toBlockedRoomEntity.nickname
        capacity = this@toBlockedRoomEntity.capacity
    }


fun BlockedRoomsEntity.toBlockedRoomResponse(): BlockedRoomResponse =
    BlockedRoomResponse(
        roomUUid = this@toBlockedRoomResponse.id.value.toString(),
        roomNumber = this@toBlockedRoomResponse.roomNumber,
        roomDescription = this@toBlockedRoomResponse.roomDescription,
        isCoedRoom = this@toBlockedRoomResponse.isCoedRoom,
        hasLovers = this@toBlockedRoomResponse.hasLovers,
        isQuiet = this@toBlockedRoomResponse.isQuiet,
        isSocial = this@toBlockedRoomResponse.isSocial,
        orientation = this@toBlockedRoomResponse.orientation.toString(),
        nickname = this@toBlockedRoomResponse.nickname,
        capacity = this@toBlockedRoomResponse.capacity
    )


fun BlockedRoomsEntity.updateFromBlockedRoomRequest(request: BlockedRoomRequest) {
    roomNumber = request.roomNumber
    roomDescription = request.roomDescription
    isCoedRoom = request.isCoedRoom
    hasLovers = request.hasLovers
    isQuiet = request.isQuiet
    isSocial = request.isSocial
    orientation = RoomOrientation.valueOf(request.orientation)
    nickname = request.nickname
    capacity = request.capacity
}