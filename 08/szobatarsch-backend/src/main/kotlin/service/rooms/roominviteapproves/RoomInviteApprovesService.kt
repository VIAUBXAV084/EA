package hu.bme.kszk.service.rooms.roominviteapproves
/*
import hu.bme.kszk.db.rooms.RoomInviteApprovesEntity
import hu.bme.kszk.db.rooms.RoomInviteApprovesTable
import hu.bme.kszk.dto.rooms.RoomInviteApprovesRequest
import hu.bme.kszk.dto.rooms.toRoomInviteApprovesEntity
import hu.bme.kszk.service.rooms.roominviterequest.RoomInviteRequestsServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class RoomInviteApprovesService: RoomInviteApprovesServiceInterface {
    override fun getAllRoomInviteApprovals(): List<RoomInviteApprovesEntity>
        = transaction { RoomInviteApprovesEntity.all().toList() }

    override fun getRoomInviteApprovalById(uuid: String): RoomInviteApprovesEntity?
        = transaction { RoomInviteApprovesEntity.findById(UUID.fromString(uuid)) }

    override fun getRoomInviteApprovalById(uuid: UUID): RoomInviteApprovesEntity?
        = transaction { RoomInviteApprovesEntity.findById(uuid) }

    override fun getRoomInviteApprovalByApproverUserId(approverUserId: UUID): RoomInviteApprovesEntity?
        = transaction { RoomInviteApprovesEntity.find { RoomInviteApprovesTable.approverUser eq approverUserId}; null }

    override fun createRoomInviteApproval(
        roomInviteApprovesRequest: RoomInviteApprovesRequest,
        userService: UsersServiceInterface,
        roomInviteRequestService: RoomInviteRequestsServiceInterface
    ): RoomInviteApprovesEntity? = transaction {
        TODO("Not yet implemented")
        return@transaction roomInviteApprovesRequest.toRoomInviteApprovesEntity(roomInviteRequestService, userService)
    }

    override fun updateRoomInviteApproval(
        updateRoomInviteApproves: RoomInviteApprovesRequest,
        userService: UsersServiceInterface,
        roomInviteRequestService: RoomInviteRequestsServiceInterface
    ): RoomInviteApprovesEntity? {
        TODO("Not yet implemented")
    }

    override fun deleteRoomInviteApproval(uuid: String): Unit? {
        TODO("Not yet implemented")
    }

}*/