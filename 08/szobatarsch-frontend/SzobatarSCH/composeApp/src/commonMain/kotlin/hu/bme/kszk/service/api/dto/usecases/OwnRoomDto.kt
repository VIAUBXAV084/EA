package hu.bme.kszk.service.api.dto.usecases


import hu.bme.kszk.service.api.dto.rooms.RoomResponse
import hu.bme.kszk.service.api.dto.users.UserResponse
import kotlinx.serialization.Serializable

@Serializable
data class OwnRoomResponse(
    val roomMembers : List<UserResponse>,
    val compatibleRooms: List<RoomResponse>
)