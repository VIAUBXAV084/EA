package hu.bme.kszk.service.users.userreports

import hu.bme.kszk.db.users.UserReportEntity
import hu.bme.kszk.dto.users.UserReportRequest
import hu.bme.kszk.service.users.users.UsersServiceInterface
import java.util.*

interface UserReportsServiceInterface {
    fun getAllUserReports(): List<UserReportEntity>
    fun getUserReportById(uuid: String): UserReportEntity?
    fun getUserReportById(uuid: UUID): UserReportEntity?
    fun getUserReportByUserId(uuid: String): UserReportEntity?
    fun createUserReport(createUserReportRequest: UserReportRequest, usersService: UsersServiceInterface): UserReportEntity
    fun updateUserReport(updateUserReportRequest: UserReportRequest, usersService: UsersServiceInterface): UserReportEntity?
    fun deleteUserReport(uuid: String): Unit
}