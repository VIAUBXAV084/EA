package hu.bme.kszk.dto.usecases

import hu.bme.kszk.dto.users.ContactInfoResponse
import hu.bme.kszk.dto.users.UserPreferenceResponse
import hu.bme.kszk.dto.users.UserResponse
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val user : UserResponse? = null,
    val contacts: List<ContactInfoResponse>,
    val preferences: UserPreferenceResponse? = null
)