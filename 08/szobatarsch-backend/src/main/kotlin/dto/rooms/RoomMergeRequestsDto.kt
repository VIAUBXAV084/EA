package hu.bme.kszk.dto.rooms

import hu.bme.kszk.db.rooms.RoomMergeRequestsEntity
import hu.bme.kszk.dto.users.UserResponse
import hu.bme.kszk.dto.users.toUserResponse
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import hu.bme.kszk.szobatarsch.enums.ApprovalStates
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class RoomMergeRequestsRequest(
    val roomMergeRequestsUuid: String="",
    val requesterUuid: String,
    val sourceRoomUuid: String,
    val targetRoomUuid: String,
    val state: String
)

@Serializable
data class RoomMergeRequestsResponse(
    val roomMergeRequestsUuid: String,
    val requester: UserResponse,
    val sourceRoom: RoomResponse,
    val targetRoom: RoomResponse,
    val state: String
)

fun RoomMergeRequestsRequest.toRoomMergeRequestsEntity(
    usersService: UsersServiceInterface,
    roomsService: RoomsServiceInterface
): RoomMergeRequestsEntity= RoomMergeRequestsEntity.new {
    requesterUser = usersService.getUserById(this@toRoomMergeRequestsEntity.requesterUuid)!!
    sourceRoom = roomsService.getRoomById(this@toRoomMergeRequestsEntity.sourceRoomUuid)!!
    targetRoom = roomsService.getRoomById(this@toRoomMergeRequestsEntity.targetRoomUuid)!!
    state = ApprovalStates.valueOf(this@toRoomMergeRequestsEntity.state)
}

fun RoomMergeRequestsEntity.toRoomMergeRequestsResponse(
    usersService: UsersServiceInterface,
    roomsService: RoomsServiceInterface
):RoomMergeRequestsResponse=RoomMergeRequestsResponse(
    roomMergeRequestsUuid = this.id.value.toString(),
    requester = transaction { usersService.getUserById(this@toRoomMergeRequestsResponse.requesterUser.id.value)!!.toUserResponse()},
    sourceRoom = transaction { roomsService.getRoomById(this@toRoomMergeRequestsResponse.sourceRoom.id.value)!!.toRoomResponse()},
    targetRoom =  transaction { roomsService.getRoomById(this@toRoomMergeRequestsResponse.targetRoom.id.value)!!.toRoomResponse()},
    state = this@toRoomMergeRequestsResponse.state.toString()
)

fun RoomMergeRequestsEntity.updateFromRoomMergeRequestsRequest(
    roomMergeRequests: RoomMergeRequestsRequest,
    usersService: UsersServiceInterface,
    roomService: RoomsServiceInterface
){
    requesterUser=usersService.getUserById(roomMergeRequests.requesterUuid)!!
    sourceRoom = roomService.getRoomById(roomMergeRequests.sourceRoomUuid)!!
    targetRoom = roomService.getRoomById(roomMergeRequests.targetRoomUuid)!!
    state = ApprovalStates.valueOf(roomMergeRequests.state)
}