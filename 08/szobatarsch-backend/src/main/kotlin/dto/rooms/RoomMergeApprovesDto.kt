package hu.bme.kszk.dto.rooms
//TODO OK
import hu.bme.kszk.db.rooms.RoomMergeApprovesEntity
import hu.bme.kszk.dto.users.UserResponse
import hu.bme.kszk.dto.users.toUserResponse
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.rooms.roommergerequest.RoomMergeRequestServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import hu.bme.kszk.szobatarsch.enums.ApprovalStates
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class RoomMergeApprovesRequest(
    val roomMergeApprovesUuid: String="",
    val roomMergeRequestUuid: String,
    val approverUuid: String,
    val state: String
)

@Serializable
data class RoomMergeApprovesResponse(
    val roomMergeApprovesUuid : String,
    val roomMergeRequest : RoomMergeRequestsResponse,
    val approver : UserResponse,
    val state : String
)

fun RoomMergeApprovesRequest.toRoomMergeApprovesEntity (
    roomsMergeRequestService: RoomMergeRequestServiceInterface,
    usersService : UsersServiceInterface,
): RoomMergeApprovesEntity = RoomMergeApprovesEntity.new{
    roomMergeRequest = roomsMergeRequestService.getRoomMergeRequestById(this@toRoomMergeApprovesEntity.roomMergeRequestUuid)!!
    approverUser = usersService.getUserById(this@toRoomMergeApprovesEntity.approverUuid)!!
    state = ApprovalStates.valueOf(this@toRoomMergeApprovesEntity.state)
}

fun RoomMergeApprovesEntity.toRoomMergeApprovesResponse(
    roomsMergeRequestService: RoomMergeRequestServiceInterface,
    roomsService: RoomsServiceInterface,
    usersService : UsersServiceInterface,
) : RoomMergeApprovesResponse=RoomMergeApprovesResponse(
    roomMergeApprovesUuid = this.id.value.toString(),
    roomMergeRequest = transaction { roomsMergeRequestService.getRoomMergeRequestById(this@toRoomMergeApprovesResponse.roomMergeRequest.id.value)!!.toRoomMergeRequestsResponse(usersService, roomsService)},
    approver = transaction { usersService.getUserById(this@toRoomMergeApprovesResponse.approverUser.id.value)!!.toUserResponse()},
    state = this@toRoomMergeApprovesResponse.state.toString()
)

fun RoomMergeApprovesEntity.updateFromRoomMergeApprovesRequest(
    roomMergeApprovesRequest : RoomMergeApprovesRequest,
    roomsMergeRequestService: RoomMergeRequestServiceInterface,
    usersService : UsersServiceInterface,
){
    roomMergeRequest =roomsMergeRequestService.getRoomMergeRequestById(roomMergeApprovesRequest.roomMergeRequestUuid)!!
    approverUser = usersService.getUserById(roomMergeApprovesRequest.approverUuid)!!
    state = ApprovalStates.valueOf(roomMergeApprovesRequest.state)
}
