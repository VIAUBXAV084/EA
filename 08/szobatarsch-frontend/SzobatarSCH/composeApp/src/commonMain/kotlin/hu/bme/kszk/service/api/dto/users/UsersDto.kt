package hu.bme.kszk.service.api.dto.users


import hu.bme.kszk.service.api.dto.rooms.RoomResponse
import kotlinx.serialization.Serializable


//TODO NOTE: If it has default value, the value isn't serialize
@Serializable
data class UserRequest(
    val userUuid: String = "",
    val schacc: String,
    val name: String,
    val email: String,
    val roomUuid: String? = null,
    val nickname: String = "",
    val profileDescription: String = "",
    val gender: String? = null,
    val major: String?  = null,
    val wantsCoedRoom: Boolean = false,
    val isMentor: Boolean = false,
    val authSchId: String? = null,
    val isSearchingRoom: Boolean = false,
)


@Serializable
data class UserResponse(
    val userUuid: String,
    val schacc: String,
    val name: String,
    val email: String,
    val room: RoomResponse?,
    val nickname: String = "",
    val profileDescription: String = "",
    val gender: String,
    val major: String,
    val wantsCoedRoom: Boolean,
    val isMentor: Boolean,
    val authSchId: String = "",
    val isSearchingRoom: Boolean = false,
)
