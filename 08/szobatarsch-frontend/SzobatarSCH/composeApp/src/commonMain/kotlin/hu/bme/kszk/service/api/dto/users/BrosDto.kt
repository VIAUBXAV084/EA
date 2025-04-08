package hu.bme.kszk.service.api.dto.users


import kotlinx.serialization.Serializable

@Serializable
data class BroState(
    val state: String?,
)

@Serializable
data class BrosResponse(
    val broUuid : String,
    val inviterUser: UserResponse,
    val targetUser: UserResponse,
    val state: String
)


@Serializable
data class BrosRequest(
    val broUuid : String = "",
    val inviterUserUuid: String,
    val targetUserUuid: String,
    val state: String
)

