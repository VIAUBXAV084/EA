package hu.bme.kszk.dto.usecases

import hu.bme.kszk.dto.rooms.RoomResponse
import hu.bme.kszk.dto.users.UserResponse
import kotlinx.serialization.Serializable

@Serializable
data class OwnRoomResponse(
    val roomMembers : List<UserResponse>,
    val compatibleRooms: List<RoomResponse>
)