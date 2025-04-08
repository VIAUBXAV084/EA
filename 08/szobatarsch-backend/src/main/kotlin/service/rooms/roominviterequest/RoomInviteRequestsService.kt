package hu.bme.kszk.service.rooms.roominviterequest

import hu.bme.kszk.db.rooms.RoomInviteRequestsEntity
import hu.bme.kszk.db.rooms.RoomInviteRequestsTable
import hu.bme.kszk.dto.rooms.RoomInviteRequestsRequest
import hu.bme.kszk.dto.rooms.toRoomInviteRequestsEntity
import hu.bme.kszk.dto.rooms.updateFromRoomInviteRequestsRequest
import hu.bme.kszk.dto.rooms.updateFromRoomMergeRequestsRequest
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.usecases.UsecasesServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class RoomInviteRequestsService : RoomInviteRequestsServiceInterface {
    override fun getAllRoomInviteRequests(): List<RoomInviteRequestsEntity> = transaction { RoomInviteRequestsEntity.all().toList() }

    override fun getRoomInviteRequest(uuid: String): RoomInviteRequestsEntity? = transaction { RoomInviteRequestsEntity.findById(UUID.fromString(uuid)) }

    override fun getRoomInviteRequest(uuid: UUID): RoomInviteRequestsEntity? = transaction { RoomInviteRequestsEntity.findById(uuid) }

    override fun getRoomInviteRequestsByInviterId(uuid: UUID): List<RoomInviteRequestsEntity> = transaction { RoomInviteRequestsEntity.find { RoomInviteRequestsTable.inviterUser eq uuid }.toList() }

    override fun getRoomInviteRequestsByInvitedId(uuid: UUID): List<RoomInviteRequestsEntity> = transaction { RoomInviteRequestsEntity.find { RoomInviteRequestsTable.invitedUser eq uuid }.toList() }

    override fun getRoomInviteRequestsByTargetRoomId(uuid: UUID): RoomInviteRequestsEntity? = transaction { RoomInviteRequestsEntity.find { RoomInviteRequestsTable.targetRoom eq uuid }; null }

    override fun createRoomInviteRequest(
        createRoomInviteRequestsRequest: RoomInviteRequestsRequest,
        usersService: UsersServiceInterface,
        roomsService: RoomsServiceInterface,
        roomInviteRequestService: RoomInviteRequestsServiceInterface,
        usecasesService: UsecasesServiceInterface
    ): RoomInviteRequestsEntity? = transaction {
        val roomMemberCounts = usecasesService.getRoomMemberCount(usersService);
        val roomMembers = roomMemberCounts[createRoomInviteRequestsRequest.targetRoomUUID];

        if(roomsService.getRoomById(createRoomInviteRequestsRequest.targetRoomUUID)?.capacity!! > roomMembers!!){
            return@transaction createRoomInviteRequestsRequest.toRoomInviteRequestsEntity(usersService, roomsService)
        }
        null
    }

    override fun updateRoomInviteRequest(
        updateRoomInviteRequestsRequest: RoomInviteRequestsRequest,
        usersService: UsersServiceInterface,
        roomInviteRequestService: RoomInviteRequestsServiceInterface,
        usecasesService: UsecasesServiceInterface,
        roomsService: RoomsServiceInterface
    ): RoomInviteRequestsEntity? = transaction {
        val roomMemberCounts = usecasesService.getRoomMemberCount(usersService);
        val roomMembers = roomMemberCounts[updateRoomInviteRequestsRequest.targetRoomUUID];

        if(roomsService.getRoomById(updateRoomInviteRequestsRequest.targetRoomUUID)?.capacity != roomMembers){
            return@transaction getRoomInviteRequest(updateRoomInviteRequestsRequest.roomInviteRequestsUUID)?.apply{updateFromRoomInviteRequestsRequest(updateRoomInviteRequestsRequest, usersService, roomsService)}
        }
        null
    }

    override fun deleteRoomInviteRequest(uuid: String): Unit = transaction {
        getRoomInviteRequest(uuid)?.delete()
    }

}