package hu.bme.kszk.routes.rooms

import hu.bme.kszk.dto.rooms.HiddenRoomRequest
import hu.bme.kszk.dto.rooms.toHiddenRoomResponse
import hu.bme.kszk.service.rooms.hidden.HiddenRoomServiceInterface
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
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
@Resource("api/v1/hidden_rooms")
class HiddenRoomsRoutes {

    @Resource("room/{id}")
    class UserUuid(val parent: HiddenRoomsRoutes = HiddenRoomsRoutes(), val id: String)

    @Resource("id/{id}")
    class HiddenRoomUuid(val parent: HiddenRoomsRoutes, val id: String)
}

fun Route.hiddenRoomsRoutes(hiddenRoomsService: HiddenRoomServiceInterface, usersService: UsersServiceInterface, roomsService: RoomsServiceInterface) {

    get<HiddenRoomsRoutes> {
        hiddenRoomsService.getAllHiddenRooms().map { hiddenRoom -> hiddenRoom.toHiddenRoomResponse(roomsService, usersService) }.let { call.respond(HttpStatusCode.OK, it) }
    }

    get<HiddenRoomsRoutes.UserUuid> { roomUuid ->
        hiddenRoomsService.getHiddenRoomsByUserId(roomUuid.id).map { it.toHiddenRoomResponse(roomsService, usersService) }.let {
            call.respond(HttpStatusCode.OK, it)
        }
    }
    get<HiddenRoomsRoutes.HiddenRoomUuid> { hiddenRoomUuid ->
        hiddenRoomsService.getHiddenRoomByOwnId(hiddenRoomUuid.id)?.toHiddenRoomResponse(roomsService, usersService)?.let {
            call.respond(HttpStatusCode.OK, it)
        } ?: call.respond( HttpStatusCode.NotFound, "The room id given is invalid (no such room exists or format is incorrect)")
    }


    post<HiddenRoomsRoutes> {
        val newHiddenRoom = call.receive<HiddenRoomRequest>()
        hiddenRoomsService.createHiddenRoom(newHiddenRoom, roomsService, usersService).toHiddenRoomResponse(roomsService, usersService).let { call.respond(HttpStatusCode.Created, it) }
    }

    put<HiddenRoomsRoutes> {
        val hiddenRoom = call.receive<HiddenRoomRequest>()
        hiddenRoomsService.updateHiddenRoom(hiddenRoom, roomsService, usersService)?.toHiddenRoomResponse(roomsService, usersService)?.let {
            call.respond(HttpStatusCode.OK, it)
        } ?: call.respond(HttpStatusCode.NotFound, "The provided hiddenRoom id is invalid")
    }

    delete<HiddenRoomsRoutes.HiddenRoomUuid> { roomHiddenRoomId ->
        hiddenRoomsService.deleteHiddenRoom(roomHiddenRoomId.id).let { call.respond(HttpStatusCode.NoContent) }
    }
}