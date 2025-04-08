package hu.bme.kszk.service.usecases

import hu.bme.kszk.db.rooms.RoomsEntity
import hu.bme.kszk.db.users.UsersEntity
import hu.bme.kszk.dto.rooms.RoomResponse
import hu.bme.kszk.service.rooms.hidden.HiddenRoomServiceInterface
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.users.bros.BroServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface


interface UsecasesServiceInterface {
    fun getMyBros(user: String, brosService: BroServiceInterface) : List<UsersEntity>
    fun getUserRequests(user: String, brosService: BroServiceInterface) : List<UsersEntity>
    fun getOthers(user: String, brosService: BroServiceInterface, usersService: UsersServiceInterface) : List<UsersEntity>
    fun getOtherRooms(user: String, usersService: UsersServiceInterface, roomsService: RoomsServiceInterface, hiddenRoomService: HiddenRoomServiceInterface): List<RoomsEntity>
    fun getRoomMembers(user: String, usersService: UsersServiceInterface): List<UsersEntity>
    fun getRoomMemberCount(usersService: UsersServiceInterface): Map<String, Int>
    fun getCompatibleRooms(
        user: String,
        roomsService: RoomsServiceInterface,
        usersService: UsersServiceInterface,
        hiddenRoomService: HiddenRoomServiceInterface
    ): List<RoomsEntity>
}