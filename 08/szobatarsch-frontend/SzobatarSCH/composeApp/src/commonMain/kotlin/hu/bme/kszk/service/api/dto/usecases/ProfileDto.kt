package hu.bme.kszk.service.api.dto.usecases


import hu.bme.kszk.service.api.dto.users.ContactInfoResponse
import hu.bme.kszk.service.api.dto.users.UserResponse
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse<UserPreferenceResponse>(
    val user : UserResponse? = null,
    val contacts: List<ContactInfoResponse>,
    val preferences: UserPreferenceResponse? = null
)