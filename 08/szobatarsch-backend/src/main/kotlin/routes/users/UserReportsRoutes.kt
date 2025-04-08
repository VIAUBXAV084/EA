package hu.bme.kszk.routes.users

import hu.bme.kszk.dto.users.UserReportRequest
import hu.bme.kszk.dto.users.toUserReportResponse
import hu.bme.kszk.service.users.userreports.UserReportsServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.resources.delete
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("api/v1/userreports")
class UserReportsRoutes {

    @Resource("id/{id}")
    class UserReportUuid(val parent: UserReportsRoutes = UserReportsRoutes(), val id: String)

    @Resource("user/{id}")
    class UserReportUserUuid(val parent: UserReportsRoutes = UserReportsRoutes(), val id: String)
}

fun Route.userReportsRoutes(
    userReportsService: UserReportsServiceInterface,
    usersService: UsersServiceInterface
) {

    get<UserReportsRoutes> {
        userReportsService.getAllUserReports()
            .map { it.toUserReportResponse(usersService) }
            .let { call.respond(HttpStatusCode.OK, it) }
    }

    get<UserReportsRoutes.UserReportUuid> { userReportRoute ->
        userReportsService.getUserReportById(userReportRoute.id)?.toUserReportResponse(usersService)?.let {
            call.respond(HttpStatusCode.OK, it)
        } ?: call.respond(HttpStatusCode.NotFound, "The user report ID given is invalid")
    }

    get<UserReportsRoutes.UserReportUserUuid> { userReportRoute ->
        userReportsService.getUserReportByUserId(userReportRoute.id)?.toUserReportResponse(usersService)?.let {
            call.respond(HttpStatusCode.OK, it)
        } ?: call.respond(HttpStatusCode.NotFound, "The user report ID given is invalid")
    }

    post<UserReportsRoutes> {
        val newUserReport = call.receive<UserReportRequest>()
        userReportsService.createUserReport(newUserReport, usersService)
            .toUserReportResponse(usersService)
            .let { call.respond(HttpStatusCode.Created, it) }
    }

    put<UserReportsRoutes> {
        val userReport = call.receive<UserReportRequest>()
        userReportsService.updateUserReport(userReport, usersService)
            ?.toUserReportResponse(usersService)
            ?.let { call.respond(HttpStatusCode.OK, it) }
            ?: call.respond(HttpStatusCode.NotFound, "The user report ID given is invalid")
    }

    delete<UserReportsRoutes.UserReportUuid> { userReportId ->
        userReportsService.deleteUserReport(userReportId.id).let {
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
