package hu.bme.kszk.routes.rooms

import hu.bme.kszk.dto.rooms.RoomRequest
import hu.bme.kszk.dto.rooms.toRoomResponse
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
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
@Resource("api/v1/rooms")
class RoomsRoutes {

    @Resource("room/{id}")
    class RoomUuid(val parent: RoomsRoutes = RoomsRoutes(), val id: String)

    @Resource("user/{userId}")
    class UserUuid(val parent: RoomsRoutes = RoomsRoutes(), val userId: String)
}

fun Route.roomRoutes(roomService: RoomsServiceInterface) {

    get<RoomsRoutes> {
        //var rooms: List<RoomResponse>? = null
        roomService.getAllRooms().map { room -> room.toRoomResponse() }.let { call.respond(HttpStatusCode.OK, it) }

        //TODO paging?
        //val rooms = roomDAO.getRooms(2, it.page) //Magic number :D Legacy
        // Ensure rooms is non-null before responding
    }

    get<RoomsRoutes.RoomUuid> { roomRoute ->
        roomService.getRoomById(roomRoute.id)?.toRoomResponse()?.let {
            call.respond(HttpStatusCode.OK, it) // Return room in response
        } ?: call.respond( HttpStatusCode.NotFound, "The room id given is invalid (no such room exists or format is incorrect)")
    }

    get<RoomsRoutes.UserUuid> { roomRoute ->
        roomService.getRoomByUserId(roomRoute.userId)?.toRoomResponse()?.let {
            call.respond(HttpStatusCode.OK, it)
        }  ?: call.respond( HttpStatusCode.NotFound, "The user id given is invalid")
    }


    post<RoomsRoutes> {
        val newRoom = call.receive<RoomRequest>()
        roomService.createRoom(newRoom).toRoomResponse().let { call.respond(HttpStatusCode.Created, it) }
    }

    put<RoomsRoutes> {
        val room = call.receive<RoomRequest>()
        roomService.updateRoom(room)?.toRoomResponse()?.let {
            call.respond(HttpStatusCode.OK, it)
        } ?: call.respond(HttpStatusCode.NotFound, "The room id given is invalid")
    }

    delete<RoomsRoutes.RoomUuid> { roomId ->
        roomService.deleteRoom(roomId.id).let { call.respond(HttpStatusCode.NoContent) }
    }
}