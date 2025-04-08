package hu.bme.kszk.db.rooms

import hu.bme.kszk.db.MAX_VARCHAR_LENGTH

import hu.bme.kszk.szobatarsch.enums.RoomOrientation
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.UUID


object BlockedRoomsTable : UUIDTable("BlockedRooms") {
    val roomNumber = integer("room_number")
    val roomDescription = text("room_description")
    val isCoedRoom = bool("is_coed_room").default(false)
    val hasLovers = bool("has_lovers").default(false)
    val isQuiet = bool("is_quiet").default(false)
    val isSocial = bool("is_social").default(false)
    val orientation = enumeration("orientation", RoomOrientation::class)
    val nickname = varchar("nickname", MAX_VARCHAR_LENGTH)

    val capacity = integer("capacity") // hány fős a szoba, a mentorok állíthatják a saját szobájukon

}


class BlockedRoomsEntity(uuid: EntityID<UUID>) : UUIDEntity(uuid) {
    companion object : UUIDEntityClass<BlockedRoomsEntity>(BlockedRoomsTable)

    var roomNumber by BlockedRoomsTable.roomNumber
    var roomDescription by BlockedRoomsTable.roomDescription
    var isCoedRoom by BlockedRoomsTable.isCoedRoom
    var hasLovers by BlockedRoomsTable.hasLovers
    var isQuiet by BlockedRoomsTable.isQuiet
    var isSocial by BlockedRoomsTable.isSocial
    var orientation by BlockedRoomsTable.orientation
    var nickname by BlockedRoomsTable.nickname
    var capacity by BlockedRoomsTable.capacity
}
