package hu.bme.kszk.service.rooms.roominviteapproves

import hu.bme.kszk.db.rooms.RoomInviteApprovesEntity
import hu.bme.kszk.db.rooms.RoomInviteRequestsEntity
import hu.bme.kszk.dto.rooms.RoomInviteApprovesRequest
import hu.bme.kszk.dto.rooms.RoomInviteRequestsRequest
import hu.bme.kszk.dto.rooms.RoomInviteRequestsResponse
import hu.bme.kszk.service.rooms.roominviterequest.RoomInviteRequestsServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface

import java.util.*

interface RoomInviteApprovesServiceInterface {
    fun getAllRoomInviteApprovals(): List<RoomInviteApprovesEntity>
    fun getRoomInviteApprovalById(uuid:String):RoomInviteApprovesEntity?
    fun getRoomInviteApprovalById(uuid: UUID):RoomInviteApprovesEntity?
    fun getRoomInviteApprovalByApproverUserId(approverUserId:UUID):RoomInviteApprovesEntity?
    fun createRoomInviteApproval(
        roomInviteApprovesRequest: RoomInviteApprovesRequest,
        userService: UsersServiceInterface,
        roomInviteRequestService: RoomInviteRequestsServiceInterface
    ):RoomInviteApprovesEntity?
    fun updateRoomInviteApproval(
        updateRoomInviteApproves: RoomInviteApprovesRequest,
        userService: UsersServiceInterface,
        roomInviteRequestService: RoomInviteRequestsServiceInterface
    ):RoomInviteApprovesEntity?
    fun deleteRoomInviteApproval(uuid: String):Unit?

}