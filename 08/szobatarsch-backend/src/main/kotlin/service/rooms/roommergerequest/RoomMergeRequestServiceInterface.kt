package hu.bme.kszk.service.rooms.roommergerequest


import hu.bme.kszk.db.rooms.RoomMergeRequestsEntity
import hu.bme.kszk.dto.rooms.RoomMergeRequestsRequest
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.usecases.UsecasesServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import java.util.UUID

interface RoomMergeRequestServiceInterface {
    fun getAllRoomMergeRequest(): List<RoomMergeRequestsEntity>
    fun getRoomMergeRequestById(uuid: String): RoomMergeRequestsEntity?
    fun getRoomMergeRequestById(uuid: UUID): RoomMergeRequestsEntity?
    fun createRoomMergeRequest(createRoomMergeRequestRequest: RoomMergeRequestsRequest, roomsService: RoomsServiceInterface, usersService: UsersServiceInterface, usecasesService: UsecasesServiceInterface): RoomMergeRequestsEntity?
    fun updateRoomMergeRequest(updateRoomMergeRequestRequest: RoomMergeRequestsRequest, roomsService: RoomsServiceInterface, usersService: UsersServiceInterface, usecasesService: UsecasesServiceInterface): RoomMergeRequestsEntity?
    fun deleteRoomMergeRequestById(uuid: String): Unit
}