package hu.bme.kszk.db.rooms

import hu.bme.kszk.db.MAX_VARCHAR_LENGTH
import hu.bme.kszk.db.rooms.RoomsTable.default
import hu.bme.kszk.db.users.UsersEntity
import hu.bme.kszk.db.users.UsersTable

import hu.bme.kszk.szobatarsch.enums.RoomOrientation
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.UUID


object RoomReportsTable : UUIDTable("RoomReports") {
    val reportedRoom = reference("reported_room", RoomsTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val reportingUser = reference("reporting_user", UsersTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val reason = varchar("reason", MAX_VARCHAR_LENGTH)
    val seenByAdmin = bool("seen_by_admin").default(false)
}


class RoomReportEntity(uuid: EntityID<UUID>) : UUIDEntity(uuid) {
    companion object : UUIDEntityClass<RoomReportEntity>(RoomReportsTable)

    var reportedRoom by RoomsEntity referencedOn RoomReportsTable.reportedRoom
    var reportingUser by UsersEntity referencedOn RoomReportsTable.reportingUser
    var reason by RoomReportsTable.reason
    var seenByAdmin by RoomReportsTable.seenByAdmin
}
