package hu.bme.kszk.service.api.dto.rooms
//TODO OK

import hu.bme.kszk.service.api.dto.users.UserResponse

import kotlinx.serialization.Serializable

@Serializable
data class RoomMergeApprovesRequest(
    val roomMergeApprovesUuid: String="",
    val roomMergeRequestUuid: String,
    val approverUuid: String,
    val state: String
)

@Serializable
data class RoomMergeApprovesResponse(
    val roomMergeApprovesUuid : String,
    val roomMergeRequest : RoomMergeRequestsResponse,
    val approver : UserResponse,
    val state : String
)
