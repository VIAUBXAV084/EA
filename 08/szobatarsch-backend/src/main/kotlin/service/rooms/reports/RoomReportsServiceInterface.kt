package hu.bme.kszk.service.rooms.reports

import hu.bme.kszk.db.rooms.RoomReportEntity
import hu.bme.kszk.dto.rooms.RoomReportRequest
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import java.util.*

interface RoomReportsServiceInterface {
    fun getAllReports(): List<RoomReportEntity>
    fun getReportsByRoomId(uuid: String): List<RoomReportEntity>
    fun getReportsByRoomId(uuid: UUID): List<RoomReportEntity>
    fun getReportByOwnId(uuid: String): RoomReportEntity?
    fun getReportByOwnId(uuid: UUID): RoomReportEntity?
    fun createReport(req: RoomReportRequest, roomsService: RoomsServiceInterface, usersService: UsersServiceInterface): RoomReportEntity
    fun updateReport(req: RoomReportRequest, roomsService: RoomsServiceInterface, usersService: UsersServiceInterface): RoomReportEntity?
    fun deleteReport(uuid: UUID)
    fun deleteReport(uuid: String)
}