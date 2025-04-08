package hu.bme.kszk.dto.users

import hu.bme.kszk.db.users.AdminsEntity
import hu.bme.kszk.service.users.users.UsersServiceInterface
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*


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


fun AdminRequest.toAdminsEntity(usersService: UsersServiceInterface) : AdminsEntity {
    return AdminsEntity.new {
        user=usersService.getUserById(this@toAdminsEntity.userUuid)!!
    }
}


fun AdminsEntity.toAdminsResponse() : AdminResponse =
    AdminResponse(
        adminUuid = this.id.value.toString(),
        user = transaction { this@toAdminsResponse.user.toUserResponse() },
    )


fun AdminsEntity.updateFromAdminRequest(updateAdminRequest: AdminRequest, userService: UsersServiceInterface) {
    user=userService.getUserById(updateAdminRequest.userUuid)!!
}