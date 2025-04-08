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
//TODO If there is an invite from the same inviter, same invited user, same room do not create a new one
object RoomInviteRequestsTable : UUIDTable("RoomInviteRequests") {
    val inviterUser = reference("inviter_user", UsersTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE).nullable()
    val invitedUser = reference("invited_room", UsersTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE).nullable()
    val targetRoom = reference("target_room", RoomsTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE).nullable()
    val state = enumeration("state", RoomInviteRequestStates::class)
}


class RoomInviteRequestsEntity(uuid: EntityID<UUID>) : UUIDEntity(uuid) {
    companion object : UUIDEntityClass<RoomInviteRequestsEntity>(RoomInviteRequestsTable)

    var inviterUser by UsersEntity optionalReferencedOn  RoomInviteRequestsTable.inviterUser
    var invitedUser by UsersEntity optionalReferencedOn RoomInviteRequestsTable.invitedUser
    var targetRoom by RoomsEntity optionalReferencedOn RoomInviteRequestsTable.targetRoom
    var state by RoomInviteRequestsTable.state
}