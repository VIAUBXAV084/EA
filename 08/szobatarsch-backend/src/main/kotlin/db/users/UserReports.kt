package hu.bme.kszk.db.users

import hu.bme.kszk.szobatarsch.enums.UserReportState
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.*

object UserReportsTable : UUIDTable("UserReports") {
    val reporterUser = reference("reporter_user", UsersTable, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
    val reportedUser = reference("reported_user", UsersTable, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
    val reason = text("reason")
    val state = enumeration("state", UserReportState::class)
}

class UserReportEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserReportEntity>(UserReportsTable)

    var reporterUser by UsersEntity referencedOn UserReportsTable.reporterUser
    var reportedUser by UsersEntity referencedOn UserReportsTable.reportedUser
    var reason by UserReportsTable.reason
    var state by UserReportsTable.state
}