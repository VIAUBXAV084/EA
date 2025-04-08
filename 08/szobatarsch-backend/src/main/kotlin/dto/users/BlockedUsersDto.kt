package hu.bme.kszk.dto.users

import hu.bme.kszk.db.users.BlockedUserEntity
import hu.bme.kszk.service.users.userreports.UserReportsServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class BlockedUserResponse(
    val blockedUserUuid: String,
    val user: UserResponse,
    val report: UserReportResponse
)

@Serializable
data class BlockedUserRequest(
    val blockedUserUuid: String = "",
    val userUuid: String,
    val reportUuid: String
)

fun BlockedUserRequest.toBlockedUserEntity(
    usersService: UsersServiceInterface,
    reportsService: UserReportsServiceInterface
): BlockedUserEntity = BlockedUserEntity.new {
    user = usersService.getUserById(this@toBlockedUserEntity.userUuid)!!
    report = reportsService.getUserReportById(this@toBlockedUserEntity.reportUuid)!!
}

fun BlockedUserEntity.toBlockedUserResponse(
    usersService: UsersServiceInterface,
    reportsService: UserReportsServiceInterface
): BlockedUserResponse = BlockedUserResponse(
    blockedUserUuid = this.id.value.toString(),
    user = transaction { usersService.getUserById(this@toBlockedUserResponse.user.id.value)!!.toUserResponse() },
    report = transaction { reportsService.getUserReportById(this@toBlockedUserResponse.report.id.value)!!.toUserReportResponse(usersService) }
)

fun BlockedUserEntity.updateFromBlockedUserRequest(
    blockedUserRequest: BlockedUserRequest,
    usersService: UsersServiceInterface,
    reportsService: UserReportsServiceInterface
) {
    user = usersService.getUserById(blockedUserRequest.userUuid)!!
    report = reportsService.getUserReportById(blockedUserRequest.reportUuid)!!
}
