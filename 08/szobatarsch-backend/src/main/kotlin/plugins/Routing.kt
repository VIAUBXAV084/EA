package hu.bme.kszk.plugins

import blockedRoomRoutes
import hu.bme.kszk.routes.reports.roomReportsRoutes
import hu.bme.kszk.routes.rooms.*
import hu.bme.kszk.routes.usecases.broingRoutes
import hu.bme.kszk.routes.usecases.discoverRoutes
import hu.bme.kszk.routes.usecases.ownRoomRoutes
import hu.bme.kszk.routes.usecases.profileRoutes
import hu.bme.kszk.routes.users.*
import hu.bme.kszk.service.MainServiceInterface
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(service: MainServiceInterface) {
    install(Resources)
    routing {
        get("/healthz") {
            call.respond(HttpStatusCode.OK)
        }
        authenticate("auth-sch") {
            hiddenRoomsRoutes(service.hiddenRoomService, service.usersService, service.roomsService)
        }


        roomRoutes(service.roomsService)
        blockedRoomRoutes(service.blockedRoomsService)
        roomReportsRoutes(service.roomReportsService, service.usersService, service.roomsService)
        roomInviteRequestsRoutes(service.roomInviteRequestsService, service.usersService, service.roomsService, service.usecasesService)

        userRoutes(service.usersService, service.roomsService)
        contactInfoRoutes(service.contactInfoService, service.usersService)
        broRoutes(service.broService, service.usersService)
        userPreferenceRoutes(service.usersPreferencesService, service.usersService)
        userReportsRoutes(service.userReportsService, service.usersService)
        blockedUsersRoutes(service.blockedUsersService, service.usersService, service.userReportsService)
        adminRoutes(service.adminService, service.usersService)

        discoverRoutes(
            service.roomsService,
            service.usersService,
            service.broService,
            service.usecasesService,
            service.hiddenRoomService
        )
        broingRoutes(service.broService, service.usersService, service.usecasesService)
        ownRoomRoutes(
            service.roomsService,
            service.usersService,
            service.usecasesService,
            service.hiddenRoomService
        )
        profileRoutes(service.usersService, service.usersPreferencesService, service.contactInfoService)
        roomMergeApprovesRoutes(
            service.roomMergeApprovesService,
            service.roomMergeRequestService,
            service.usersService,
            service.roomsService,
            service.usecasesService
        )
        roomMergeRequestRoutes(
            service.roomMergeRequestService,
            service.usersService,
            service.roomsService,
            service.usecasesService
        )

    }
}

