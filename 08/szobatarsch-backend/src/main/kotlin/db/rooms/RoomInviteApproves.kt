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

object RoomInviteApprovesTable : UUIDTable("RoomInviteApproves") {
    val roomInviteRequest = reference("room_invite_request", RoomInviteRequestsTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE).nullable()
    val approverUser = reference("approver_user", UsersTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE).nullable()
    val state = enumeration("state", ApprovalStates::class)
}


class RoomInviteApprovesEntity(uuid: EntityID<UUID>) : UUIDEntity(uuid) {
    companion object : UUIDEntityClass<RoomInviteApprovesEntity>(RoomInviteApprovesTable)

    var roomInviteRequest by RoomInviteRequestsEntity optionalReferencedOn  RoomInviteApprovesTable.roomInviteRequest
    var approverUser by UsersEntity optionalReferencedOn RoomInviteApprovesTable.approverUser
    var state by RoomInviteApprovesTable.state
}