package hu.bme.kszk.dto.users

import hu.bme.kszk.db.users.UserReportEntity
import hu.bme.kszk.service.users.users.UsersServiceInterface
import hu.bme.kszk.szobatarsch.enums.UserReportState
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction

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

fun UserReportRequest.toUserReportEntity(usersService: UsersServiceInterface): UserReportEntity =
    UserReportEntity.new {
        reporterUser = usersService.getUserById(this@toUserReportEntity.reporterUserUuid)!!
        reportedUser = usersService.getUserById(this@toUserReportEntity.reportedUserUuid)!!
        reason = this@toUserReportEntity.reason
        state = UserReportState.valueOf(this@toUserReportEntity.state)
    }

fun UserReportEntity.toUserReportResponse(usersService: UsersServiceInterface): UserReportResponse =
    UserReportResponse(
        userReportUuid = this.id.value.toString(),
        reporterUser = transaction { usersService.getUserById(this@toUserReportResponse.reporterUser.id.value)!!.toUserResponse() },
        reportedUser = transaction { usersService.getUserById(this@toUserReportResponse.reportedUser.id.value)!!.toUserResponse() },
        reason = this.reason,
        state = this.state.toString()
    )

fun UserReportEntity.updateFromUserReportRequest(
    userReportRequest: UserReportRequest,
    usersService: UsersServiceInterface
) {
    reporterUser = usersService.getUserById(userReportRequest.reporterUserUuid)!!
    reportedUser = usersService.getUserById(userReportRequest.reportedUserUuid)!!
    reason = userReportRequest.reason
    state = UserReportState.valueOf(userReportRequest.state)
}
