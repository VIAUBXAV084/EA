package hu.bme.kszk.service.api.dto.rooms


import hu.bme.kszk.service.api.dto.users.UserResponse

import kotlinx.serialization.Serializable


@Serializable
data class RoomReportResponse(
    val reportUuid: String,
    val reportedRoom: RoomResponse,
    val reportingUser: UserResponse,
    val reason: String,
    val seenByAdmin: Boolean
)

@Serializable
data class RoomReportRequest(
    val roomReportUuid: String = "",
    val roomUuid: String,
    val reportingUserUuid: String,
    val reason: String,
    val seenByAdmin: Boolean = false
)


