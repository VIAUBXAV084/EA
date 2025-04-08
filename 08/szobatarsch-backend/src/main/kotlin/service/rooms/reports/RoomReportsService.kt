package hu.bme.kszk.service.rooms.reports

import hu.bme.kszk.db.rooms.RoomReportEntity
import hu.bme.kszk.db.rooms.RoomReportsTable
import hu.bme.kszk.dto.rooms.RoomReportRequest
import hu.bme.kszk.dto.rooms.toRoomReportEntity
import hu.bme.kszk.dto.rooms.updateFromRoomReportRequest
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class RoomReportsService : RoomReportsServiceInterface {
    override fun getAllReports(): List<RoomReportEntity> = transaction { RoomReportEntity.all().toList() }
    override fun getReportsByRoomId(uuid: String) : List<RoomReportEntity> = transaction { RoomReportEntity.wrapRows(RoomReportsTable.selectAll().where { RoomReportsTable.reportedRoom eq UUID.fromString(uuid) }).toList() }
    override fun getReportsByRoomId(uuid: UUID) : List<RoomReportEntity> = transaction { RoomReportEntity.wrapRows(RoomReportsTable.selectAll().where { RoomReportsTable.reportedRoom eq uuid }).toList() }
    override fun getReportByOwnId(uuid: String) : RoomReportEntity? = transaction { RoomReportsTable.selectAll().where { RoomReportsTable.id eq UUID.fromString(uuid) }.firstOrNull()?.let { RoomReportEntity.wrapRow(it) } }
    override fun getReportByOwnId(uuid: UUID) : RoomReportEntity? = transaction { RoomReportsTable.selectAll().where { RoomReportsTable.id eq uuid }.firstOrNull()?.let { RoomReportEntity.wrapRow(it) } }
    override fun createReport(
        req: RoomReportRequest, roomsService: RoomsServiceInterface, usersService: UsersServiceInterface
    ) : RoomReportEntity = transaction { req.toRoomReportEntity(roomsService, usersService) }
    override fun updateReport(req: RoomReportRequest, roomsService: RoomsServiceInterface, usersService: UsersServiceInterface) : RoomReportEntity? =
        transaction {
            getReportByOwnId(req.roomReportUuid)?.apply { updateFromRoomReportRequest(req, roomsService, usersService) }
        }
    override fun deleteReport(uuid: UUID) : Unit = transaction { RoomReportsTable.deleteWhere { RoomReportsTable.id eq uuid} }
    override fun deleteReport(uuid: String) : Unit = transaction { RoomReportsTable.deleteWhere { RoomReportsTable.id eq UUID.fromString(uuid) } }
}