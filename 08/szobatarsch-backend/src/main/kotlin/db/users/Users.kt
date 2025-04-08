package hu.bme.kszk.db.users

import hu.bme.kszk.db.MAX_VARCHAR_LENGTH
import hu.bme.kszk.db.rooms.RoomsEntity
import hu.bme.kszk.db.rooms.RoomsTable
import hu.bme.kszk.enums.Gender
import hu.bme.kszk.enums.Major
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.UUID


object UsersTable : UUIDTable("Users") {
    val schacc = varchar("schacc", MAX_VARCHAR_LENGTH)      //AuthSchból
    val name = varchar("name", MAX_VARCHAR_LENGTH)          //AuthSchból
    val email = varchar("email", MAX_VARCHAR_LENGTH)        //AuthSchból
    val nickname = varchar("nickname", MAX_VARCHAR_LENGTH).default("")
    val profileDescription = text("profile_description").default("")
    val room = reference("room", RoomsTable.id, ReferenceOption.SET_NULL, ReferenceOption.CASCADE).nullable()
    val gender = enumeration("gender", Gender::class).default(Gender.MALE)
    val major = enumeration("major", Major::class).default(Major.MI).nullable()
    val wantsCoedRoom =  bool("wants_coed_room").default(false)
    val isMentor = bool("is_mentor").default(false)

    val authSchId = varchar("auth_schid", MAX_VARCHAR_LENGTH).default("")
    val isSearchingRoom = bool("is_searching_room").default(false)
}

class UsersEntity(uuid: EntityID<UUID>) : UUIDEntity(uuid) {
    companion object : UUIDEntityClass<UsersEntity>(UsersTable)

    var schacc by UsersTable.schacc
    var name by UsersTable.name
    var email by UsersTable.email
    var nickname by UsersTable.nickname
    var profileDescription by UsersTable.profileDescription
    var room by RoomsEntity optionalReferencedOn UsersTable.room
    var gender by UsersTable.gender
    var major by UsersTable.major
    var wantsCoedRoom by UsersTable.wantsCoedRoom
    var isMentor by UsersTable.isMentor

    var authSchId by UsersTable.authSchId
    var isSearchingRoom by UsersTable.isSearchingRoom
}
