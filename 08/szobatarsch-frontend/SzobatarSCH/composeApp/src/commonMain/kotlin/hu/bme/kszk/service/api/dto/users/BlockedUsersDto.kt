package hu.bme.kszk.service.api.dto.users


import kotlinx.serialization.Serializable


@Serializable
data class BlockedUserResponse(
    val blockedUserUuid: String,
    val user: UserResponse,
    val report: UserReportResponse
)

@Serializable
data class BlockedUserRequest(
    val blockedUserUuid: String = "",
    val userUuid: String,
    val reportUuid: String
)
