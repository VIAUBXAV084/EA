package hu.bme.kszk.db.rooms

import hu.bme.kszk.db.MAX_VARCHAR_LENGTH

import hu.bme.kszk.szobatarsch.enums.RoomOrientation
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.UUID


object RoomsTable : UUIDTable("Rooms") {
    val roomNumber = integer("room_number").nullable()
    val roomDescription = text("room_description")
    val isCoedRoom = bool("is_coed_room").default(false)
    val hasLovers = bool("has_lovers").default(false)
    val isQuiet = bool("is_quiet").default(false)
    val isSocial = bool("is_social").default(false)
    val orientation = enumeration("orientation", RoomOrientation::class)
    val nickname = varchar("nickname", MAX_VARCHAR_LENGTH)

    val capacity = integer("capacity") // hány fős a szoba, a mentorok állíthatják a saját szobájukon

}


class RoomsEntity(uuid: EntityID<UUID>) : UUIDEntity(uuid) {
    companion object : UUIDEntityClass<RoomsEntity>(RoomsTable)

    var roomNumber by RoomsTable.roomNumber
    var roomDescription by RoomsTable.roomDescription
    var isCoedRoom by RoomsTable.isCoedRoom
    var hasLovers by RoomsTable.hasLovers
    var isQuiet by RoomsTable.isQuiet
    var isSocial by RoomsTable.isSocial
    var orientation by RoomsTable.orientation
    var nickname by RoomsTable.nickname
    var capacity by RoomsTable.capacity
}
