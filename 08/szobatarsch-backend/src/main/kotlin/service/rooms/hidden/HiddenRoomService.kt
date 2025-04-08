package hu.bme.kszk.service.rooms.hidden

import hu.bme.kszk.db.rooms.HiddenRoomsEntity
import hu.bme.kszk.db.rooms.HiddenRoomsTable
import hu.bme.kszk.db.rooms.RoomReportsTable
import hu.bme.kszk.dto.rooms.HiddenRoomRequest
import hu.bme.kszk.dto.rooms.toHiddenRoomEntity
import hu.bme.kszk.dto.rooms.toHiddenRoomResponse
import hu.bme.kszk.dto.rooms.updateFromHiddenRoomRequest
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class HiddenRoomService : HiddenRoomServiceInterface {
    override fun getAllHiddenRooms(): List<HiddenRoomsEntity> = transaction { HiddenRoomsEntity.all().toList() }
    override fun getHiddenRoomsByUserId(uuid: String): List<HiddenRoomsEntity> = transaction {
        HiddenRoomsEntity.find { HiddenRoomsTable.hiderUser eq UUID.fromString(uuid) }.toList()
    }

    override fun getHiddenRoomsByUserId(uuid: UUID): List<HiddenRoomsEntity> = transaction {
        HiddenRoomsEntity.find { HiddenRoomsTable.hiderUser eq uuid }.toList()
    }

    override fun getHiddenRoomByOwnId(uuid: String): HiddenRoomsEntity? = transaction { HiddenRoomsEntity.findById(UUID.fromString(uuid)) }

    override fun getHiddenRoomByOwnId(uuid: UUID): HiddenRoomsEntity? = transaction { HiddenRoomsEntity.findById(uuid) }

    override fun createHiddenRoom(
        req: HiddenRoomRequest,
        roomsService: RoomsServiceInterface,
        usersService: UsersServiceInterface
    ): HiddenRoomsEntity = transaction {
        req.toHiddenRoomEntity(roomsService, usersService)
    }

    override fun updateHiddenRoom(
        req: HiddenRoomRequest,
        roomsService: RoomsServiceInterface,
        usersService: UsersServiceInterface
    ): HiddenRoomsEntity? = transaction { getHiddenRoomByOwnId(req.hiddenRoomUuid)?.apply { updateFromHiddenRoomRequest(req ,roomsService, usersService) } }

    override fun deleteHiddenRoom(uuid: UUID): Unit = transaction {
        HiddenRoomsTable.deleteWhere { HiddenRoomsTable.id eq uuid}
    }

    override fun deleteHiddenRoom(uuid: String): Unit = transaction { HiddenRoomsTable.deleteWhere { HiddenRoomsTable.id eq UUID.fromString(uuid) } }
}