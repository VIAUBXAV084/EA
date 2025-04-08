package hu.bme.kszk.dto.rooms


import hu.bme.kszk.db.rooms.RoomInviteRequestsEntity
import hu.bme.kszk.dto.users.UserResponse
import hu.bme.kszk.dto.users.toUserResponse
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import hu.bme.kszk.szobatarsch.enums.ApprovalStates
import hu.bme.kszk.szobatarsch.enums.RoomInviteRequestStates
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction


@Serializable
data class RoomInviteRequestsRequest(
    val roomInviteRequestsUUID: String="",
    val inviterUUID: String,
    val invitedUUID: String,
    val targetRoomUUID: String,
    val state: String
)

@Serializable
data class RoomInviteRequestsResponse(
    val roomInviteRequestsUUID: String,
    val inviter: UserResponse? = null,
    val invited: UserResponse? = null,
    val targetRoom: RoomResponse? = null,
    val state: String
)

fun RoomInviteRequestsRequest.toRoomInviteRequestsEntity (
    usersService: UsersServiceInterface,
    roomService: RoomsServiceInterface,
): RoomInviteRequestsEntity = RoomInviteRequestsEntity.new{
    inviterUser = usersService.getUserById(this@toRoomInviteRequestsEntity.inviterUUID)
    invitedUser = usersService.getUserById(this@toRoomInviteRequestsEntity.invitedUUID)
    targetRoom = roomService.getRoomById(this@toRoomInviteRequestsEntity.targetRoomUUID)
    state = RoomInviteRequestStates.valueOf(this@toRoomInviteRequestsEntity.state)
}

fun RoomInviteRequestsEntity.toRoomInviteRequestsResponse(
    usersService: UsersServiceInterface,
    roomService: RoomsServiceInterface,
) : RoomInviteRequestsResponse=RoomInviteRequestsResponse(
    roomInviteRequestsUUID = this.id.value.toString(),
    inviter = transaction { this@toRoomInviteRequestsResponse.inviterUser?.id?.value?.let { usersService.getUserById(it) }
        ?.toUserResponse() },
    invited = transaction { this@toRoomInviteRequestsResponse.invitedUser?.id?.value?.let { usersService.getUserById(it) }
            ?.toUserResponse() },
    targetRoom = transaction { this@toRoomInviteRequestsResponse.targetRoom?.id?.value?.let { roomService.getRoomById(it) }
        ?.toRoomResponse()  },
    state = this@toRoomInviteRequestsResponse.state.toString(),
)

fun RoomInviteRequestsEntity.updateFromRoomInviteRequestsRequest(
    roomInviteRequests: RoomInviteRequestsRequest,
    usersService: UsersServiceInterface,
    roomService: RoomsServiceInterface,
){
    inviterUser = usersService.getUserById(roomInviteRequests.inviterUUID)
    invitedUser = usersService.getUserById(roomInviteRequests.invitedUUID)
    targetRoom = roomService.getRoomById(roomInviteRequests.targetRoomUUID)
    state = RoomInviteRequestStates.valueOf(roomInviteRequests.state)
}