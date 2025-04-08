package hu.bme.kszk.discover.ui

import hu.bme.kszk.service.api.dto.rooms.RoomResponse
import hu.bme.kszk.service.api.dto.users.UserResponse
import hu.bme.kszk.util.NetworkError

data class DiscoverScreenState(
    val rooms: List<RoomResponse> = emptyList(),
    val users: List<UserResponse> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: NetworkError? = null,
    val userId: String? = null,
)
