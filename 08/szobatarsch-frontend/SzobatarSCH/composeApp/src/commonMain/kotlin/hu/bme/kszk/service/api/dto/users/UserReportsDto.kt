package hu.bme.kszk.service.api.dto.users


import kotlinx.serialization.Serializable


@Serializable
data class UserReportResponse(
    val userReportUuid: String,
    val reporterUser: UserResponse,
    val reportedUser: UserResponse,
    val reason: String,
    val state: String
)

@Serializable
data class UserReportRequest(
    val userReportUuid: String = "",
    val reporterUserUuid: String,
    val reportedUserUuid: String,
    val reason: String,
    val state: String
)
