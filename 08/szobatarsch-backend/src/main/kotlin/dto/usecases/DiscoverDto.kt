package hu.bme.kszk.dto.usecases

import hu.bme.kszk.dto.rooms.RoomResponse
import hu.bme.kszk.dto.users.UserResponse
import kotlinx.serialization.Serializable

@Serializable
data class DiscoverResponse(
    val rooms: List<RoomResponse>,
    val users: List<UserResponse>
)
