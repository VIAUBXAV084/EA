package hu.bme.kszk.dto.users

import hu.bme.kszk.db.users.BrosEntity
import hu.bme.kszk.service.users.users.UsersServiceInterface
import hu.bme.kszk.szobatarsch.enums.BroStates
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction

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


fun BrosRequest.toBrosEntity(usersService: UsersServiceInterface) : BrosEntity =
    BrosEntity.new {
        inviterUser = usersService.getUserById(this@toBrosEntity.inviterUserUuid)!!
        targetUser = usersService.getUserById(this@toBrosEntity.targetUserUuid)!!
        state = BroStates.valueOf(this@toBrosEntity.state)
    }


fun BrosEntity.toBrosResponse(usersService: UsersServiceInterface) : BrosResponse =
   transaction {
       BrosResponse(
           broUuid = this@toBrosResponse.id.value.toString(),
           inviterUser = usersService.getUserById(this@toBrosResponse.inviterUser.id.value)!!.toUserResponse(),
           targetUser = usersService.getUserById(this@toBrosResponse.targetUser.id.value)!!.toUserResponse(),
           state = this@toBrosResponse.state.toString()
       )
   }


fun BrosEntity.updateFromBroRequest(broRequest: BrosRequest, userService: UsersServiceInterface) {
    inviterUser = userService.getUserById(broRequest.inviterUserUuid)!!
    targetUser = userService.getUserById(broRequest.targetUserUuid)!!
    state = broRequest.let { BroStates.valueOf(it.state) }
}