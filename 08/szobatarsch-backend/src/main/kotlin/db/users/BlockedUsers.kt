package hu.bme.kszk.db.users

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.*

object BlockedUsersTable : UUIDTable("BlockedUsers") {
    val user = reference("user", UsersTable, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
    val report= reference("report", UserReportsTable, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
}

class BlockedUserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<BlockedUserEntity>(BlockedUsersTable)

    var user by UsersEntity referencedOn BlockedUsersTable.user
    var report by UserReportEntity referencedOn BlockedUsersTable.report
}