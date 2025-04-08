package hu.bme.kszk.dto.rooms

import hu.bme.kszk.db.rooms.HiddenRoomsEntity
import hu.bme.kszk.dto.users.UserResponse
import hu.bme.kszk.dto.users.toUserResponse
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class HiddenRoomResponse(
    val hiddenRoomUuid: String,
    val hiderUser: UserResponse,
    val hiddenRoom: RoomResponse,
)

@Serializable
data class HiddenRoomRequest(
    val hiddenRoomUuid: String = "",
    val hiderUserUuid: String,
    val hiddenRoomRoomUuid: String,
)


fun HiddenRoomRequest.toHiddenRoomEntity(roomsService: RoomsServiceInterface, usersService: UsersServiceInterface): HiddenRoomsEntity =
    HiddenRoomsEntity.new {
        hiderUser = usersService.getUserById(this@toHiddenRoomEntity.hiderUserUuid)!!
        hiddenRoom = roomsService.getRoomById(this@toHiddenRoomEntity.hiddenRoomRoomUuid)!!
    }


fun HiddenRoomsEntity.toHiddenRoomResponse(roomsService: RoomsServiceInterface, usersService: UsersServiceInterface): HiddenRoomResponse =
    transaction {
        HiddenRoomResponse(
            hiddenRoomUuid = this@toHiddenRoomResponse.id.value.toString(),
            hiderUser = usersService.getUserById(this@toHiddenRoomResponse.hiderUser.id.value)!!.toUserResponse(),
            hiddenRoom = roomsService.getRoomById(this@toHiddenRoomResponse.hiddenRoom.id.value)!!.toRoomResponse(),
        )
    }


fun HiddenRoomsEntity.updateFromHiddenRoomRequest(request: HiddenRoomRequest, roomsService: RoomsServiceInterface, usersService: UsersServiceInterface) {
    hiderUser = usersService.getUserById(request.hiderUserUuid)!!
    hiddenRoom = roomsService.getRoomById(request.hiddenRoomRoomUuid)!!
}