package hu.bme.kszk.service.rooms.hidden

import hu.bme.kszk.db.rooms.HiddenRoomsEntity
import hu.bme.kszk.dto.rooms.HiddenRoomRequest
import hu.bme.kszk.dto.rooms.RoomReportRequest
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import java.util.*

interface HiddenRoomServiceInterface {
    fun getAllHiddenRooms(): List<HiddenRoomsEntity>
    fun getHiddenRoomsByUserId(uuid: String): List<HiddenRoomsEntity>
    fun getHiddenRoomsByUserId(uuid: UUID): List<HiddenRoomsEntity>
    fun getHiddenRoomByOwnId(uuid: String): HiddenRoomsEntity?
    fun getHiddenRoomByOwnId(uuid: UUID): HiddenRoomsEntity?
    fun createHiddenRoom(req: HiddenRoomRequest, roomsService: RoomsServiceInterface, usersService: UsersServiceInterface): HiddenRoomsEntity
    fun updateHiddenRoom(req: HiddenRoomRequest, roomsService: RoomsServiceInterface, usersService: UsersServiceInterface): HiddenRoomsEntity?
    fun deleteHiddenRoom(uuid: UUID)
    fun deleteHiddenRoom(uuid: String)
}