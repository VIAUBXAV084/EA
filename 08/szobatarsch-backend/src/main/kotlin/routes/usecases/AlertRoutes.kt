package hu.bme.kszk.routes.usecases

import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.usecases.UsecasesServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import io.ktor.resources.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("api/v1/alerts")
class AlertRoutes {}

fun Route.alertRoutes(roomsService: RoomsServiceInterface, usersService: UsersServiceInterface, usecasesService: UsecasesServiceInterface) {
    //TODO getAll alert types RoomInviteApproves RoomInviteRequests RomJoinApproves RoomJoinRequests RoomMergeRequests RoomMergeApproves Bro

}