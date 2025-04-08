package hu.bme.kszk.myRoom.ui

import hu.bme.kszk.service.api.dto.rooms.RoomResponse
import hu.bme.kszk.service.api.dto.users.UserResponse
import hu.bme.kszk.util.NetworkError

data class MyRoomScreenState(
    val room: RoomResponse? = null,
    val members: List<UserResponse> = emptyList(),
    val invitableUsers: List<UserResponse> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: NetworkError? = null,
    val userId: String? = null,
)
