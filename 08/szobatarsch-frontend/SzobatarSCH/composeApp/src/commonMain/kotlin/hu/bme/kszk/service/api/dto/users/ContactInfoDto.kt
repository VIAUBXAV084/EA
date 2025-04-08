package hu.bme.kszk.service.api.dto.users



import kotlinx.serialization.Serializable



@Serializable
data class ContactInfoResponse(
    val contactInfoUuid: String,
    val user: UserResponse, //TODO Remove or nullable?
    val contactType: String,
    val uri: String
)


@Serializable
data class ContactInfoRequest(
    val contactInfoUuid: String = "",
    val userUuid: String,
    val contactType: String,
    val uri: String
)
