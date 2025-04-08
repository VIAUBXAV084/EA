package hu.bme.kszk.service.rooms.roommergeapprove

import hu.bme.kszk.db.rooms.RoomMergeApprovesEntity
import hu.bme.kszk.dto.rooms.RoomMergeApprovesRequest
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.rooms.roommergerequest.RoomMergeRequestServiceInterface
import hu.bme.kszk.service.usecases.UsecasesServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import java.util.UUID

interface RoomMergeApprovesServiceInterface {
    fun getAllRoomMergeApprovals():List<RoomMergeApprovesEntity>
    fun getRoomMergeApprovals(uuid:String):RoomMergeApprovesEntity?
    fun getRoomMergeApprovals(uuid:UUID):RoomMergeApprovesEntity?
    fun createRoomMergeApproval(
        createRoomMergeApprovesRequest: RoomMergeApprovesRequest,
        userService: UsersServiceInterface,
        roomsService: RoomsServiceInterface,
        roomMergeRequestService: RoomMergeRequestServiceInterface,
        usecasesService: UsecasesServiceInterface
    ):RoomMergeApprovesEntity?
    fun updateRoomMergeApproval(
        updateRoomMergeApproves: RoomMergeApprovesRequest,
        userService: UsersServiceInterface,
        roomMergeRequestService: RoomMergeRequestServiceInterface,
        usecasesService: UsecasesServiceInterface,
        roomsService: RoomsServiceInterface,
    ):RoomMergeApprovesEntity?
    fun deleteRoomMergeApproval(uuid:String):Unit?
}