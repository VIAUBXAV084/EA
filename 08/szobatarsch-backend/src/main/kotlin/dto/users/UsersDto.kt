package hu.bme.kszk.dto.users

import hu.bme.kszk.db.users.UsersEntity
import hu.bme.kszk.dto.rooms.RoomResponse
import hu.bme.kszk.dto.rooms.toRoomResponse
import hu.bme.kszk.enums.Gender
import hu.bme.kszk.enums.Major
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

//TODO NOTE: If it has default value, the value isn't serialize
@Serializable
data class UserRequest(
    val userUuid: String = "",
    val schacc: String,
    val name: String,
    val email: String,
    val roomUuid: String? = null,
    val nickname: String = "",
    val profileDescription: String = "",
    val gender: String? = null,
    val major: String?  = null,
    val wantsCoedRoom: Boolean = false,
    val isMentor: Boolean = false,
    val authSchId: String? = null,
    val isSearchingRoom: Boolean = false,
)


@Serializable
data class UserResponse(
    val userUuid: String,
    val schacc: String,
    val name: String,
    val email: String,
    val room: RoomResponse?,
    val nickname: String = "",
    val profileDescription: String = "",
    val gender: String,
    val major: String,
    val wantsCoedRoom: Boolean,
    val isMentor: Boolean,
    val authSchId: String = "",
    val isSearchingRoom: Boolean = false,
)


fun UserRequest.toUserEntity(roomsService: RoomsServiceInterface): UsersEntity =
    UsersEntity.new {
        schacc = this@toUserEntity.schacc
        name = this@toUserEntity.name
        email = this@toUserEntity.email
        room = this@toUserEntity.roomUuid?.let { roomsService.getRoomById(it) }
        nickname = this@toUserEntity.nickname
        profileDescription = this@toUserEntity.profileDescription
        gender = this@toUserEntity.gender?.let { Gender.valueOf(it) }?: Gender.UNKNOWN
        major = this@toUserEntity.let { it.major?.let { it1 -> Major.valueOf(it1) } } ?: Major.UNKNOWN
        wantsCoedRoom = this@toUserEntity.wantsCoedRoom
        isMentor = this@toUserEntity.isMentor
        authSchId = this@toUserEntity.authSchId.toString()
        isSearchingRoom = this@toUserEntity.isSearchingRoom
    }


fun UsersEntity.toUserResponse(): UserResponse =
    UserResponse(
        userUuid = this.id.value.toString(),
        schacc = this.schacc,
        name = this.name,
        email = this.email,
        room = transaction { this@toUserResponse.room?.toRoomResponse() },
        nickname = this.nickname,
        profileDescription = this.profileDescription,
        gender = this.gender.toString(),
        major = this.major.toString(),
        wantsCoedRoom = this.wantsCoedRoom,
        isMentor = this.isMentor,
        authSchId = this.authSchId,
        isSearchingRoom = this.isSearchingRoom
    )


fun UsersEntity.updateFromUserRequest(userRequest: UserRequest, roomsService: RoomsServiceInterface) {
        schacc = userRequest.schacc
        name = userRequest.name
        email = userRequest.email
        room = userRequest.roomUuid?.let { println(it); roomsService.getRoomById(it) }
        nickname = userRequest.nickname
        profileDescription = userRequest.profileDescription
        gender = userRequest.gender?.let { Gender.valueOf(it) } ?: Gender.UNKNOWN
        major = userRequest.let { it.major?.let { it1 -> Major.valueOf(it1) } } ?: Major.UNKNOWN
        wantsCoedRoom = userRequest.wantsCoedRoom
        isMentor = userRequest.isMentor
        authSchId = userRequest.authSchId.toString()
        isSearchingRoom = userRequest.isSearchingRoom
}