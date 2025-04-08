package hu.bme.kszk.db.rooms

import hu.bme.kszk.szobatarsch.enums.ApprovalStates

import hu.bme.kszk.db.users.UsersEntity
import hu.bme.kszk.db.users.UsersTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.*

object RoomJoinRequestsTable : UUIDTable("RoomJoinRequests") {
    val requesterUser = reference("requester_user", UsersTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE) // the requester that wants to join the room
    val targetRoom = reference("target_room", RoomsTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE) // the room the requester wants to join
    val state = enumeration("state", ApprovalStates::class)
}


class RoomJoinRequestsEntity(uuid: EntityID<UUID>) : UUIDEntity(uuid) {
    companion object : UUIDEntityClass<RoomJoinRequestsEntity>(RoomJoinRequestsTable)

    var requesterUser by UsersEntity referencedOn RoomJoinRequestsTable.requesterUser
    var targetRoom by RoomsEntity referencedOn RoomJoinRequestsTable.targetRoom
    var state by RoomJoinRequestsTable.state
}

