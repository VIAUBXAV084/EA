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


object RoomMergeApprovesTable : UUIDTable(name = "RoomMergeRequest") {
    val roomMergeRequest = reference("roomMergeRequest", RoomMergeRequestsTable, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
    val approverUser = reference("approverUser", UsersTable, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
    val state = enumeration("state", ApprovalStates::class)
}

class RoomMergeApprovesEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<RoomMergeApprovesEntity>(RoomMergeApprovesTable)

    var roomMergeRequest by RoomMergeRequestsEntity referencedOn RoomMergeApprovesTable.roomMergeRequest
    var approverUser by UsersEntity referencedOn RoomMergeApprovesTable.approverUser
    var state by RoomMergeApprovesTable.state
}
