package hu.bme.kszk.routes.reports

import hu.bme.kszk.dto.rooms.RoomReportRequest
import hu.bme.kszk.dto.rooms.toRoomReportResponse
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.rooms.reports.RoomReportsServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("api/v1/room_reports")
class RoomReportsRoutes {

    @Resource("room/{id}")
    class RoomUuid(val parent: RoomReportsRoutes = RoomReportsRoutes(), val id: String)

    @Resource("id/{id}")
    class ReportUuid(val parent: RoomReportsRoutes, val id: String)
}

fun Route.roomReportsRoutes(roomReportsService: RoomReportsServiceInterface, usersService: UsersServiceInterface, roomsService: RoomsServiceInterface) {

    get<RoomReportsRoutes> {
        roomReportsService.getAllReports().map { report -> report.toRoomReportResponse(roomsService, usersService) }.let { call.respond(HttpStatusCode.OK, it) }
    }

    get<RoomReportsRoutes.RoomUuid> { roomUuid ->
        roomReportsService.getReportsByRoomId(roomUuid.id).map { it.toRoomReportResponse(roomsService, usersService) }.let {
            call.respond(HttpStatusCode.OK, it)
        }
    }
    get<RoomReportsRoutes.ReportUuid> { reportUuid ->
        roomReportsService.getReportByOwnId(reportUuid.id)?.toRoomReportResponse(roomsService, usersService)?.let {
            call.respond(HttpStatusCode.OK, it)
        } ?: call.respond( HttpStatusCode.NotFound, "The room id given is invalid (no such room exists or format is incorrect)")
    }


    post<RoomReportsRoutes> {
        val newReport = call.receive<RoomReportRequest>()
        roomReportsService.createReport(newReport, roomsService, usersService).toRoomReportResponse(roomsService, usersService).let { call.respond(HttpStatusCode.Created, it) }
    }

    put<RoomReportsRoutes> {
        val report = call.receive<RoomReportRequest>()
        roomReportsService.updateReport(report, roomsService, usersService)?.toRoomReportResponse(roomsService, usersService)?.let {
            call.respond(HttpStatusCode.OK, it)
        } ?: call.respond(HttpStatusCode.NotFound, "The provided report id is invalid")
    }

    delete<RoomReportsRoutes.ReportUuid> { roomReportId ->
        roomReportsService.deleteReport(roomReportId.id).let { call.respond(HttpStatusCode.NoContent) }
    }
}