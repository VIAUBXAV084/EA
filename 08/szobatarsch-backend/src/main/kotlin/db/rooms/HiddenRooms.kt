package hu.bme.kszk.db.rooms

import hu.bme.kszk.db.users.UsersEntity
import hu.bme.kszk.db.users.UsersTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.*

object HiddenRoomsTable : UUIDTable("HiddenRooms") {
    val hiderUser = reference("hider_user", UsersTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val hiddenRoom = reference("hidden_room", RoomsTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
}


class HiddenRoomsEntity(uuid: EntityID<UUID>) : UUIDEntity(uuid) {
    companion object : UUIDEntityClass<HiddenRoomsEntity>(HiddenRoomsTable)

    var hiderUser by UsersEntity referencedOn HiddenRoomsTable.hiderUser
    var hiddenRoom by RoomsEntity referencedOn HiddenRoomsTable.hiddenRoom
}
