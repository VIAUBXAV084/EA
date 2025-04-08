package hu.bme.kszk.service.users.userreports


import hu.bme.kszk.db.users.*
import hu.bme.kszk.dto.users.UserReportRequest
import hu.bme.kszk.dto.users.toUserReportEntity
import hu.bme.kszk.dto.users.updateFromUserReportRequest
import hu.bme.kszk.service.MainService.userReportsService
import hu.bme.kszk.service.users.users.UsersServiceInterface
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class UserReportsService : UserReportsServiceInterface {
    override fun getAllUserReports(): List<UserReportEntity> =
        transaction { UserReportEntity.all().toList() }

    override fun getUserReportById(uuid: String): UserReportEntity? =
        transaction { UserReportEntity.findById(UUID.fromString(uuid)) }

    override fun getUserReportById(uuid: UUID): UserReportEntity? =
        transaction { UserReportEntity.findById(uuid) }

    override fun getUserReportByUserId(uuid: String): UserReportEntity? = transaction {
        UserReportEntity.wrapRows(
            UsersTable.innerJoin(UserReportsTable).selectAll().where{ UsersTable.id eq UUID.fromString(uuid)}
        ).firstOrNull()
    }

    override fun createUserReport(
        createUserReportRequest: UserReportRequest,
        usersService: UsersServiceInterface
    ): UserReportEntity = transaction {
        createUserReportRequest.toUserReportEntity(usersService)
    }

    override fun updateUserReport(
        updateUserReportRequest: UserReportRequest,
        usersService: UsersServiceInterface
    ): UserReportEntity? = transaction {
        getUserReportById(updateUserReportRequest.userReportUuid)?.apply { getUserReportById(updateUserReportRequest.userReportUuid)?.apply { updateFromUserReportRequest(updateUserReportRequest, usersService) } }
    }

    override fun deleteUserReport(uuid: String) {
        transaction {
            getUserReportById(uuid)?.delete()
        }
    }
}