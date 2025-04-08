package hu.bme.kszk.service.api.dto.rooms

import hu.bme.kszk.service.api.dto.users.UserResponse

import kotlinx.serialization.Serializable

@Serializable
data class HiddenRoomResponse(
    val hiddenRoomUuid: String,
    val hiderUser: UserResponse,
    val hiddenRoom: RoomResponse,
)

@Serializable
data class HiddenRoomRequest(
    val hiddenRoomUuid: String = "",
    val hiderUserUuid: String,
    val hiddenRoomRoomUuid: String,
)
