package hu.bme.kszk.dto.rooms

import hu.bme.kszk.db.rooms.RoomsEntity
import hu.bme.kszk.szobatarsch.enums.RoomOrientation
import kotlinx.serialization.Serializable

@Serializable
data class RoomResponse(
    val roomUUid: String,
    val roomNumber: Int? = null,
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
data class RoomRequest(
    val roomUUid: String = "",
    val roomNumber: Int? = null,
    val roomDescription: String,
    val isCoedRoom: Boolean,
    val hasLovers: Boolean,
    val isQuiet: Boolean,
    val isSocial: Boolean,
    val orientation: String,
    val nickname: String,
    val capacity: Int
)

fun RoomRequest.toRoomEntity(): RoomsEntity =
    RoomsEntity.new {
        roomNumber = this@toRoomEntity.roomNumber
        roomDescription = this@toRoomEntity.roomDescription
        isCoedRoom = this@toRoomEntity.isCoedRoom
        hasLovers = this@toRoomEntity.hasLovers
        isQuiet = this@toRoomEntity.isQuiet
        isSocial = this@toRoomEntity.isSocial
        orientation = RoomOrientation.valueOf(this@toRoomEntity.orientation)
        nickname = this@toRoomEntity.nickname
        capacity = this@toRoomEntity.capacity
    }


fun RoomsEntity.toRoomResponse(): RoomResponse =
    RoomResponse(
        roomUUid = this@toRoomResponse.id.value.toString(),
        roomNumber = this@toRoomResponse.roomNumber,
        roomDescription = this@toRoomResponse.roomDescription,
        isCoedRoom = this@toRoomResponse.isCoedRoom,
        hasLovers = this@toRoomResponse.hasLovers,
        isQuiet = this@toRoomResponse.isQuiet,
        isSocial = this@toRoomResponse.isSocial,
        orientation = this@toRoomResponse.orientation.toString(),
        nickname = this@toRoomResponse.nickname,
        capacity = this@toRoomResponse.capacity
    )


fun RoomsEntity.updateFromRoomRequest(roomRequest: RoomRequest) {
    roomNumber = roomRequest.roomNumber
    roomDescription = roomRequest.roomDescription
    isCoedRoom = roomRequest.isCoedRoom
    hasLovers = roomRequest.hasLovers
    isQuiet = roomRequest.isQuiet
    isSocial = roomRequest.isSocial
    orientation = RoomOrientation.valueOf(roomRequest.orientation)
    nickname = roomRequest.nickname
    capacity = roomRequest.capacity
}