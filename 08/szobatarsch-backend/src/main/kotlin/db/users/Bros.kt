package hu.bme.kszk.db.users

import hu.bme.kszk.szobatarsch.enums.BroStates
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.*

object BrosTable : UUIDTable("Bros") {
    val inviterUser = reference("inviter_user", UsersTable, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
    val targetUser = reference("target_user", UsersTable, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
    val state = enumeration("state", BroStates::class)

}

class BrosEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<BrosEntity>(BrosTable)

    var inviterUser by UsersEntity referencedOn BrosTable.inviterUser
    var targetUser by UsersEntity referencedOn BrosTable.targetUser
    var state by BrosTable.state
}