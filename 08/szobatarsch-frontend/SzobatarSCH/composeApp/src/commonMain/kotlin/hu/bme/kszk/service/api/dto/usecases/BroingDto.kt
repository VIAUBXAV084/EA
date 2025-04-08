package hu.bme.kszk.service.api.dto.usecases


import hu.bme.kszk.service.api.dto.users.UserResponse
import kotlinx.serialization.Serializable

@Serializable
data class BroingResponse(
    val bros: List<UserResponse>,
    val brosRequest: List<UserResponse>,
    val otherUsers: List<UserResponse>
)