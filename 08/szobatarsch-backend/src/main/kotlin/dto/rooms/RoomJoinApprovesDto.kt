package hu.bme.kszk.dto.rooms

import hu.bme.kszk.db.rooms.RoomJoinApprovesEntity
import hu.bme.kszk.db.rooms.RoomInviteRequestsEntity
import hu.bme.kszk.dto.users.UserResponse
import hu.bme.kszk.dto.users.toUserResponse
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.rooms.roommergerequest.RoomMergeRequestServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import hu.bme.kszk.szobatarsch.enums.ApprovalStates
import hu.bme.kszk.szobatarsch.enums.RoomInviteRequestStates
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction


@Serializable
data class RoomJoinApprovesRequest(
    val roomJoinApprovesUuid: String="",
    val roomJoinRequestUuid: String,
    val approverUuid: String,
    val state: String
)

@Serializable
data class RoomJoinApprovesResponse(
    val roomJoinApprovesUUID: String,
    val roomJoinRequest: RoomJoinRequestsResponse,
    val approver: UserResponse,
    val state: String
)
/*
fun RoomJoinApprovesRequest.toRoomJoinApprovesEntity (
    roomJoinRequestsService: RoomJoinRequestsServiceInterface,
    usersService: UsersServiceInterface,
): RoomJoinApprovesEntity = RoomJoinApprovesEntity.new{
    roomJoinRequest = roomJoinRequestsService.getRoomJoinRequestsById(this@toRoomJoinApprovesEntity.roomJoinRequestUuid)!!
    approverUser = usersService.getUserById(this@toRoomJoinApprovesEntity.approverUuid)!!
    state = RoomInviteRequestStates.valueOf(this@toRoomJoinApprovesEntity.state)
}

fun RoomJoinApprovesEntity.toRoomJoinApprovesResponse(
    usersService: UsersServiceInterface,
    roomJoinRequestsService: RoomJoinRequestsServiceInterface,
) : RoomJoinApprovesResponse=RoomJoinApprovesResponse(
    roomJoinApprovesUUID = this.id.value.toString(),
    roomJoinRequest = transaction { roomJoinRequestsService.getRoomJoinRequestById(this@toRoomJoinApprovesResponse.roomJoinRequest.id.value)!!.toRoomJoinRequestsResponse(usersService, roomJoinRequestsService)},
    approver = transaction { usersService.getUserById(this@toRoomJoinApprovesResponse.approverUser.id.value)!!.toUserResponse()},
    state = this@toRoomJoinApprovesResponse.state.toString()
)

fun RoomJoinApprovesEntity.updateFromRoomJoinApprovesRequest(
    roomJoinApprovesRequest: RoomJoinApprovesRequest,
    roomJoinRequestsService: RoomJoinRequestsServiceInterface,
    usersService: UsersServiceInterface,
){
    roomJoinRequest = roomJoinRequestsService.getRoomJoinRequestById(roomJoinApprovesRequest.roomJoinRequestUuid)!!
    approverUser = usersService.getUserById(roomJoinApprovesRequest.approverUuid)!!
    state = RoomInviteRequestStates.valueOf(roomJoinApprovesRequest.state)
}*/