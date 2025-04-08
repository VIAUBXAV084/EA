package hu.bme.kszk.dto.rooms

import hu.bme.kszk.db.rooms.RoomReportEntity
import hu.bme.kszk.dto.users.UserResponse
import hu.bme.kszk.dto.users.toUserResponse
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class RoomReportResponse(
    val reportUuid: String,
    val reportedRoom: RoomResponse,
    val reportingUser: UserResponse,
    val reason: String,
    val seenByAdmin: Boolean
)

@Serializable
data class RoomReportRequest(
    val roomReportUuid: String = "",
    val roomUuid: String,
    val reportingUserUuid: String,
    val reason: String,
    val seenByAdmin: Boolean = false
)


fun RoomReportRequest.toRoomReportEntity(roomsService: RoomsServiceInterface, usersService: UsersServiceInterface): RoomReportEntity =
    RoomReportEntity.new {
        reportedRoom = roomsService.getRoomById(this@toRoomReportEntity.roomUuid)!!
        reportingUser = usersService.getUserById(this@toRoomReportEntity.reportingUserUuid)!!
        reason = this@toRoomReportEntity.reason
        seenByAdmin = this@toRoomReportEntity.seenByAdmin
    }


fun RoomReportEntity.toRoomReportResponse(roomsService: RoomsServiceInterface, usersService: UsersServiceInterface): RoomReportResponse =
    transaction {
        RoomReportResponse(
            reportUuid = this@toRoomReportResponse.id.value.toString(),
            reportedRoom = roomsService.getRoomById(this@toRoomReportResponse.reportedRoom.id.value)!!.toRoomResponse(),
            reportingUser = usersService.getUserById(this@toRoomReportResponse.reportingUser.id.value)!!.toUserResponse(),
            reason = this@toRoomReportResponse.reason,
            seenByAdmin = this@toRoomReportResponse.seenByAdmin
        )
    }


fun RoomReportEntity.updateFromRoomReportRequest(request: RoomReportRequest, roomsService: RoomsServiceInterface, usersService: UsersServiceInterface) {
    reportedRoom = roomsService.getRoomById(request.roomUuid)!!
    reportingUser = usersService.getUserById(request.reportingUserUuid)!!
    reason = request.reason
    seenByAdmin = request.seenByAdmin
}