package hu.bme.kszk.service.api.dto.users



import kotlinx.serialization.Serializable


@Serializable
data class AdminResponse(
    val adminUuid: String,
    val user: UserResponse
)


@Serializable
data class AdminRequest(
    val adminUuid: String = "",
    val userUuid: String
)