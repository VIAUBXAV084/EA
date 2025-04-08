package hu.bme.kszk.dto.usecases

import hu.bme.kszk.dto.users.BrosRequest
import hu.bme.kszk.dto.users.BrosResponse
import hu.bme.kszk.dto.users.UserResponse
import kotlinx.serialization.Serializable

@Serializable
data class BroingResponse(
    val bros: List<UserResponse>,
    val brosRequest: List<UserResponse>,
    val otherUsers: List<UserResponse>
)