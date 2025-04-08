package hu.bme.kszk.dto.rooms

import hu.bme.kszk.db.rooms.RoomInviteApprovesEntity
import hu.bme.kszk.db.rooms.RoomMergeApprovesEntity
import hu.bme.kszk.dto.users.UserResponse
import hu.bme.kszk.dto.users.toUserResponse
import hu.bme.kszk.service.MainService.usersService
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.rooms.roominviterequest.RoomInviteRequestsServiceInterface
import hu.bme.kszk.service.rooms.roommergerequest.RoomMergeRequestServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import hu.bme.kszk.szobatarsch.enums.ApprovalStates
import hu.bme.kszk.szobatarsch.enums.RoomInviteRequestStates
import kotlinx.html.Entities
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class RoomInviteApprovesRequest(
    val roomInviteApprovesUuid: String="",
    val roomInviteRequestUuid: String,
    val approverUserUuid: String,
    val state: String
)

@Serializable
data class RoomInviteApprovesResponse(
    val roomInviteApprovesUuid: String,
    val roomInviteRequest: RoomInviteRequestsResponse?,
    val approverUser: UserResponse,
    val state: String
)
/*
fun RoomInviteApprovesRequest.toRoomInviteApprovesEntity (
    roomInviteRequestService: RoomInviteRequestsServiceInterface,
    usersService: UsersServiceInterface
): RoomInviteApprovesEntity = RoomInviteApprovesEntity.new{
    roomInviteRequest = roomInviteRequestService.getRoomInviteRequests(this@toRoomInviteApprovesEntity.roomInviteRequestUuid)
    approverUser = usersService.getUserById(this@toRoomInviteApprovesEntity.approverUserUuid)
    state = ApprovalStates.valueOf(this@toRoomInviteApprovesEntity.state)
}

fun RoomInviteApprovesEntity.toRoomInviteApprovesResponse(
    userService: UsersServiceInterface,
    roomService: RoomsServiceInterface
) : RoomInviteApprovesResponse=RoomInviteApprovesResponse(
    roomInviteApprovesUuid =this@toRoomInviteApprovesResponse.id.value.toString(),
    roomInviteRequest = transaction { this@toRoomInviteApprovesResponse.roomInviteRequest?.toRoomInviteRequestsResponse(userService, roomService) },
    approverUser = transaction { this@toRoomInviteApprovesResponse.approverUser?.id?.let { userService.getUserById(it.value) }!!.toUserResponse() },
    state = this@toRoomInviteApprovesResponse.state.toString()
)

fun RoomInviteApprovesEntity.updateFromRoomInviteApprovesRequest(
    roomIntviteRequest: RoomInviteApprovesRequest,
    roomInviteRequestService: RoomInviteRequestsServiceInterface,
    userService: UsersServiceInterface
){
    roomInviteRequest = roomInviteRequestService.getRoomInviteRequests(roomIntviteRequest.roomInviteRequestUuid)
    approverUser =userService.getUserById(roomIntviteRequest.roomInviteRequestUuid)
    state = ApprovalStates.valueOf(roomIntviteRequest.state)
}*/