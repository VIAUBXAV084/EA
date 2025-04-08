package hu.bme.kszk.service.api.dto.rooms




import hu.bme.kszk.service.api.dto.users.UserResponse
import kotlinx.serialization.Serializable

@Serializable
data class RoomMergeRequestsRequest(
    val roomMergeRequestsUuid: String="",
    val requesterUuid: String,
    val sourceRoomUuid: String,
    val targetRoomUuid: String,
    val state: String
)

@Serializable
data class RoomMergeRequestsResponse(
    val roomMergeRequestsUuid: String,
    val requester: UserResponse,
    val sourceRoom: RoomResponse,
    val targetRoom: RoomResponse,
    val state: String
)

