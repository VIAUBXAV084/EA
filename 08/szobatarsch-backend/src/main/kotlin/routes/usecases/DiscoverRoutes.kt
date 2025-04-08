package hu.bme.kszk.routes.usecases

import hu.bme.kszk.db.users.UsersEntity
import hu.bme.kszk.dto.rooms.toRoomResponse
import hu.bme.kszk.dto.usecases.DiscoverResponse
import hu.bme.kszk.dto.users.toUserResponse
import hu.bme.kszk.service.rooms.hidden.HiddenRoomServiceInterface
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.usecases.UsecasesServiceInterface
import hu.bme.kszk.service.users.bros.BroServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import io.ktor.server.response.*


@Serializable
@Resource("api/v1/discover/{id}")
class DiscoverRoutes(val id: String)

fun Route.discoverRoutes(roomsService: RoomsServiceInterface, usersService: UsersServiceInterface, brosService: BroServiceInterface, usecasesService: UsecasesServiceInterface, hiddenRoomService: HiddenRoomServiceInterface) {
    get<DiscoverRoutes>{ user ->
        val others: MutableList<UsersEntity> = mutableListOf()

        others.addAll(usecasesService.getOthers(user.id, brosService, usersService))

        DiscoverResponse(usecasesService.getOtherRooms(user.id, usersService, roomsService, hiddenRoomService).map { it.toRoomResponse() }, others.map { it.toUserResponse() }).let { call.respond(HttpStatusCode.OK, it) }
    }
}