import hu.bme.kszk.dto.rooms.BlockedRoomRequest
import hu.bme.kszk.dto.rooms.toBlockedRoomResponse
import hu.bme.kszk.service.rooms.blocked.BlockedRoomsServiceInterface
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
@Resource("api/v1/blocked_rooms")
class BlockedRoomsRoutes {
    @Serializable
    @Resource("{id}")
    class RoomUuid(val parent: BlockedRoomsRoutes, val id: String)
}

fun Route.blockedRoomRoutes(blockedRoomService: BlockedRoomsServiceInterface) {
    get<BlockedRoomsRoutes> {
        blockedRoomService.getAllBlocked().map { room -> room.toBlockedRoomResponse() }.let { call.respond(HttpStatusCode.OK, it) }
    }

    get<BlockedRoomsRoutes.RoomUuid> { blockedRoomRoute ->
        blockedRoomService.getBlockedRoomById(blockedRoomRoute.id)?.toBlockedRoomResponse()?.let {
            call.respond(HttpStatusCode.OK, it) // Return room in response
        } ?: call.respond( HttpStatusCode.NotFound, "The room id given is invalid (no such room exists in the blocked list or the format is incorrect)")
    }


    post<BlockedRoomsRoutes> {
        val newRoom = call.receive<BlockedRoomRequest>()
        blockedRoomService.createBlockedRoom(newRoom).toBlockedRoomResponse().let { call.respond(HttpStatusCode.Created, it) }
    }

    put<BlockedRoomsRoutes> {
        val room = call.receive<BlockedRoomRequest>()
        blockedRoomService.updateBlockedRoom(room)?.toBlockedRoomResponse()?.let {
            call.respond(HttpStatusCode.OK, it)
        } ?: call.respond(HttpStatusCode.NotFound, "The room id given is invalid")
    }

    delete<BlockedRoomsRoutes.RoomUuid> { roomId ->
        blockedRoomService.deleteBlockedRoom(roomId.id).let { call.respond(HttpStatusCode.NoContent) }
    }
}