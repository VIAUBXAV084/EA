package hu.bme.kszk.db.rooms

import hu.bme.kszk.db.users.UsersEntity
import hu.bme.kszk.db.users.UsersTable
import hu.bme.kszk.szobatarsch.enums.ApprovalStates
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.*

object RoomMergeRequestsTable : UUIDTable(name = "RoomMergeRequests") {
    val requesterUser = reference("requesterUser",UsersTable, onDelete = ReferenceOption.CASCADE,onUpdate = ReferenceOption.CASCADE)
    val sourceRoom = reference("sourceRoom",RoomsTable, onDelete = ReferenceOption.CASCADE,onUpdate = ReferenceOption.CASCADE)
    val targetRoom =reference("targetRoom",RoomsTable, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
    val state = enumeration("state", ApprovalStates::class)
}

class RoomMergeRequestsEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<RoomMergeRequestsEntity>(RoomMergeRequestsTable)

    var requesterUser by UsersEntity referencedOn RoomMergeRequestsTable.requesterUser
    var sourceRoom by RoomsEntity referencedOn RoomMergeRequestsTable.sourceRoom
    var targetRoom by RoomsEntity referencedOn RoomMergeRequestsTable.targetRoom
    var state by RoomMergeRequestsTable.state
}