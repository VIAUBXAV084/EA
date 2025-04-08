package hu.bme.kszk.routes.rooms


import hu.bme.kszk.dto.rooms.RoomMergeRequestsRequest
import hu.bme.kszk.dto.rooms.toRoomMergeRequestsResponse
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.rooms.roommergerequest.RoomMergeRequestServiceInterface
import hu.bme.kszk.service.usecases.UsecasesServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import io.ktor.resources.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.resources.delete
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

@Serializable
@Resource ("api/v1/roommergerequest")
class RoomMergeRequestRoutes {

    @Resource("{id}")
    class RoomMergeRequestUuid(val parent: RoomMergeRequestRoutes = RoomMergeRequestRoutes(), val id: String)
}

fun Route.roomMergeRequestRoutes(
    roomMergeRequestService: RoomMergeRequestServiceInterface,
    usersService: UsersServiceInterface,
    roomsService: RoomsServiceInterface,
    usecasesService: UsecasesServiceInterface
) {
    get<RoomMergeRequestRoutes>{
        roomMergeRequestService.getAllRoomMergeRequest()
            .map { it.toRoomMergeRequestsResponse(usersService, roomsService) }
        .let { call.respond(HttpStatusCode.OK, it) }
    }
    get<RoomMergeRequestRoutes.RoomMergeRequestUuid>{ response->
        roomMergeRequestService.getRoomMergeRequestById(response.id)
            ?.toRoomMergeRequestsResponse(usersService,roomsService)
            ?.let { call.respond(HttpStatusCode.OK, it) }
                ?:call.respond(HttpStatusCode.NotFound, "Room merge request not found")

    }

    post<RoomMergeRequestRoutes>{
        val roomsMergeRequest = call.receive<RoomMergeRequestsRequest>()
        roomMergeRequestService.createRoomMergeRequest(roomsMergeRequest,roomsService, usersService, usecasesService )
            ?.toRoomMergeRequestsResponse(usersService,roomsService)
            ?.let { call.respond(HttpStatusCode.Created, it) }
            ?: call.respond(HttpStatusCode.BadRequest)
    }


    put<RoomMergeRequestRoutes>{
        val roomMergeRequest = call.receive<RoomMergeRequestsRequest>()
        roomMergeRequestService.updateRoomMergeRequest(roomMergeRequest,roomsService,usersService, usecasesService)
            ?.toRoomMergeRequestsResponse(usersService,roomsService)
            ?.let { call.respond(HttpStatusCode.OK, it) }
            ?: call.respond(HttpStatusCode.BadRequest)
    }

    delete<RoomMergeRequestRoutes.RoomMergeRequestUuid>{ roomsMergeRequestUuid->
        roomMergeRequestService.deleteRoomMergeRequestById(roomsMergeRequestUuid.id)
            .let {call.respond(HttpStatusCode.OK, it)}
    }
}