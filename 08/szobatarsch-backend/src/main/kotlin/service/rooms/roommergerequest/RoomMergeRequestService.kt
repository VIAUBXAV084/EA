package hu.bme.kszk.service.rooms.roommergerequest

import hu.bme.kszk.db.rooms.RoomMergeRequestsEntity
import hu.bme.kszk.dto.rooms.*
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.usecases.UsecasesService
import hu.bme.kszk.service.usecases.UsecasesServiceInterface
import hu.bme.kszk.service.users.users.UsersService
import hu.bme.kszk.service.users.users.UsersServiceInterface
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class RoomMergeRequestService : RoomMergeRequestServiceInterface {
    override fun getAllRoomMergeRequest(): List<RoomMergeRequestsEntity> = transaction { RoomMergeRequestsEntity.all().toList() }
    override fun getRoomMergeRequestById(uuid: String): RoomMergeRequestsEntity? = transaction { RoomMergeRequestsEntity.findById(UUID.fromString(uuid))}
    override fun getRoomMergeRequestById(uuid: UUID): RoomMergeRequestsEntity? = transaction { RoomMergeRequestsEntity.findById(uuid) }
    override fun createRoomMergeRequest(createRoomMergeRequestRequest: RoomMergeRequestsRequest, roomsService: RoomsServiceInterface, usersService: UsersServiceInterface, usecasesService: UsecasesServiceInterface): RoomMergeRequestsEntity? = transaction {
        val roomMemberCounts =  usecasesService.getRoomMemberCount(usersService)

        roomMemberCounts[createRoomMergeRequestRequest.targetRoomUuid]?.let {
            roomMemberCounts[createRoomMergeRequestRequest.sourceRoomUuid]?.plus(
                it
            )
        }?.let {
            if( it <= (roomsService.getRoomById(createRoomMergeRequestRequest.targetRoomUuid)?.capacity ?: -1) ){
                return@transaction createRoomMergeRequestRequest.toRoomMergeRequestsEntity(usersService, roomsService)
            }
        }
        null
    }
    override fun updateRoomMergeRequest(updateRoomMergeRequestRequest: RoomMergeRequestsRequest, roomsService: RoomsServiceInterface, usersService: UsersServiceInterface, usecasesService: UsecasesServiceInterface): RoomMergeRequestsEntity? = transaction {
        val roomMemberCounts =  usecasesService.getRoomMemberCount(usersService)

        roomMemberCounts[updateRoomMergeRequestRequest.targetRoomUuid]?.let {
            roomMemberCounts[updateRoomMergeRequestRequest.sourceRoomUuid]?.plus(
                it
            )
        }?.let {
            if( it <= (roomsService.getRoomById(updateRoomMergeRequestRequest.targetRoomUuid)?.capacity ?: -1) ){
                return@transaction getRoomMergeRequestById(updateRoomMergeRequestRequest.roomMergeRequestsUuid)?.apply{updateFromRoomMergeRequestsRequest(updateRoomMergeRequestRequest, usersService, roomsService)}
            }
        }
        null

    }
    override fun deleteRoomMergeRequestById(uuid: String): Unit = transaction { getRoomMergeRequestById(uuid)?.delete() }
}