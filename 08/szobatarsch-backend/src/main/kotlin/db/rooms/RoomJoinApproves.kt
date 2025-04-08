package hu.bme.kszk.db.rooms

import hu.bme.kszk.db.users.UsersEntity
import hu.bme.kszk.db.users.UsersTable
import hu.bme.kszk.szobatarsch.enums.RoomInviteRequestStates
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.*

object RoomJoinApprovesTable : UUIDTable("RoomJoinApproves") {
    val roomJoinRequest = reference("room_join_request", RoomJoinRequestsTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val approverUser = reference("approver_user", UsersTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val state = enumeration("state", RoomInviteRequestStates::class)

}


class RoomJoinApprovesEntity(uuid: EntityID<UUID>) : UUIDEntity(uuid) {
    companion object : UUIDEntityClass<RoomJoinApprovesEntity>(RoomJoinApprovesTable)

    var roomJoinRequest by RoomJoinRequestsEntity referencedOn RoomJoinApprovesTable.roomJoinRequest
    var approverUser by UsersEntity referencedOn RoomJoinApprovesTable.approverUser
    var state by RoomJoinApprovesTable.state
}