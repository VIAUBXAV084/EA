package hu.bme.kszk.service.rooms.roominviterequest

import hu.bme.kszk.db.rooms.RoomInviteRequestsEntity
import hu.bme.kszk.dto.rooms.RoomInviteRequestsRequest
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.usecases.UsecasesServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import java.util.*

interface RoomInviteRequestsServiceInterface {
    fun getAllRoomInviteRequests():List<RoomInviteRequestsEntity>
    fun getRoomInviteRequest(uuid:String): RoomInviteRequestsEntity?
    fun getRoomInviteRequest(uuid: UUID): RoomInviteRequestsEntity?
    fun getRoomInviteRequestsByInviterId(uuid: UUID): List<RoomInviteRequestsEntity>
    fun getRoomInviteRequestsByInvitedId(uuid: UUID): List<RoomInviteRequestsEntity>
    fun getRoomInviteRequestsByTargetRoomId(uuid: UUID): RoomInviteRequestsEntity?

    fun createRoomInviteRequest(
        createRoomInviteRequestsRequest: RoomInviteRequestsRequest,
        usersService: UsersServiceInterface,
        roomsService: RoomsServiceInterface,
        roomInviteRequestService: RoomInviteRequestsServiceInterface,
        usecasesService: UsecasesServiceInterface
    ): RoomInviteRequestsEntity?
    fun updateRoomInviteRequest(
        updateRoomInviteRequestsRequest: RoomInviteRequestsRequest,
        usersService: UsersServiceInterface,
        roomInviteRequestService: RoomInviteRequestsServiceInterface,
        usecasesService: UsecasesServiceInterface,
        roomsService: RoomsServiceInterface,
    ): RoomInviteRequestsEntity?
    fun deleteRoomInviteRequest(uuid:String):Unit?
}