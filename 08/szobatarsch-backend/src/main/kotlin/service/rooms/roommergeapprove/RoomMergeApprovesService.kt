package hu.bme.kszk.service.rooms.roommergeapprove

import hu.bme.kszk.db.rooms.RoomMergeApprovesEntity
import hu.bme.kszk.dto.rooms.*
import hu.bme.kszk.service.MainService.usersService
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.rooms.roommergerequest.RoomMergeRequestServiceInterface
import hu.bme.kszk.service.usecases.UsecasesServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import hu.bme.kszk.szobatarsch.enums.ApprovalStates
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*


class RoomMergeApprovesService: RoomMergeApprovesServiceInterface {
    override fun getAllRoomMergeApprovals(): List<RoomMergeApprovesEntity> = transaction { RoomMergeApprovesEntity.all().toList() }
    override fun getRoomMergeApprovals(uuid: String): RoomMergeApprovesEntity? = transaction { RoomMergeApprovesEntity.findById(UUID.fromString(uuid))}
    override fun getRoomMergeApprovals(uuid: UUID): RoomMergeApprovesEntity? = transaction { RoomMergeApprovesEntity.findById(uuid) }
    override fun createRoomMergeApproval(
        createRoomMergeApprovesRequest: RoomMergeApprovesRequest,
        userService: UsersServiceInterface,
        roomsService: RoomsServiceInterface,
        roomMergeRequestService: RoomMergeRequestServiceInterface,
        usecasesService: UsecasesServiceInterface
    ):RoomMergeApprovesEntity? = transaction {

        val roomMemberCounts =  usecasesService.getRoomMemberCount(userService)
        val roomMergeRequest = roomMergeRequestService.getRoomMergeRequestById(createRoomMergeApprovesRequest.roomMergeRequestUuid)
        roomMemberCounts[roomMergeRequest?.targetRoom?.id?.value.toString()]?.let {
            roomMemberCounts[roomMergeRequest?.sourceRoom?.id?.value.toString()]?.plus(
                it
            )
        }?.let {
            if( it <= (roomsService.getRoomById(roomMergeRequest?.targetRoom?.id?.value.toString())?.capacity ?: -1) ){
                roomMergeRequestService.updateRoomMergeRequest(
                    RoomMergeRequestsRequest(
                        roomMergeRequestsUuid = roomMergeRequest?.id?.value.toString(),
                        requesterUuid = roomMergeRequest?.requesterUser?.id?.value.toString(),
                        sourceRoomUuid = roomMergeRequest?.sourceRoom?.id?.value.toString(),
                        targetRoomUuid = roomMergeRequest?.targetRoom?.id?.value.toString(),
                        state = ApprovalStates.valueOf(createRoomMergeApprovesRequest.state).toString()
                    ),
                    roomsService = roomsService,
                    usersService = userService,
                    usecasesService = usecasesService
                )

                return@transaction createRoomMergeApprovesRequest.toRoomMergeApprovesEntity(roomMergeRequestService, userService)
            }
        }
        null

    }

    override fun updateRoomMergeApproval(
        updateRoomMergeApproves: RoomMergeApprovesRequest,
        userService: UsersServiceInterface,
        roomMergeRequestService: RoomMergeRequestServiceInterface,
        usecasesService: UsecasesServiceInterface,
        roomsService: RoomsServiceInterface,
    ):RoomMergeApprovesEntity? = transaction {
        val roomMemberCounts =  usecasesService.getRoomMemberCount(userService)
        val roomMergeRequest = roomMergeRequestService.getRoomMergeRequestById(updateRoomMergeApproves.roomMergeRequestUuid)
        roomMemberCounts[roomMergeRequest?.targetRoom?.id?.value.toString()]?.let {
            roomMemberCounts[roomMergeRequest?.sourceRoom?.id?.value.toString()]?.plus(
                it
            )
        }?.let {
            if( it <= (roomsService.getRoomById(roomMergeRequest?.targetRoom?.id?.value.toString())?.capacity ?: -1) ){
                roomMergeRequestService.updateRoomMergeRequest(
                    RoomMergeRequestsRequest(
                        roomMergeRequestsUuid = roomMergeRequest?.id?.value.toString(),
                        requesterUuid = roomMergeRequest?.requesterUser?.id?.value.toString(),
                        sourceRoomUuid = roomMergeRequest?.sourceRoom?.id?.value.toString(),
                        targetRoomUuid = roomMergeRequest?.targetRoom?.id?.value.toString(),
                        state = ApprovalStates.valueOf(updateRoomMergeApproves.state).toString()
                    ),
                    roomsService = roomsService,
                    usersService = userService,
                    usecasesService = usecasesService
                )

                return@transaction getRoomMergeApprovals(updateRoomMergeApproves.roomMergeApprovesUuid)?.apply{updateFromRoomMergeApprovesRequest(updateRoomMergeApproves, roomMergeRequestService, userService)}
            }
        }
        null


    }

    override fun deleteRoomMergeApproval(uuid: String): Unit? = transaction { getRoomMergeApprovals(uuid)?.delete() }
}