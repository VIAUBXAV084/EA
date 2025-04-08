package hu.bme.kszk.routes.rooms

import hu.bme.kszk.dto.rooms.RoomMergeApprovesRequest
import hu.bme.kszk.dto.rooms.toRoomMergeApprovesResponse
import hu.bme.kszk.plugins.UserSession
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.rooms.roommergeapprove.RoomMergeApprovesServiceInterface
import hu.bme.kszk.service.rooms.roommergerequest.RoomMergeRequestServiceInterface
import hu.bme.kszk.service.usecases.UsecasesServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.resources.delete
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("api/v1/roommergeapproves")
class RoomMergeApprovesRoutes{

    @Resource("{id}")
    class RoomMergeApprovesUuid(val parent: RoomMergeApprovesRoutes=RoomMergeApprovesRoutes(), val id:String)
}

fun Route.roomMergeApprovesRoutes(
    roomMergeApprovesService: RoomMergeApprovesServiceInterface,
    roomMergeRequestService: RoomMergeRequestServiceInterface,
    usersService: UsersServiceInterface,
    roomService: RoomsServiceInterface,
    usecasesService: UsecasesServiceInterface
) {
    get<RoomMergeApprovesRoutes>{
        call.sessions.get<UserSession>()?.let { session -> session.userId}
        roomMergeApprovesService.getAllRoomMergeApprovals()
            .map{it.toRoomMergeApprovesResponse(roomMergeRequestService,roomService, usersService)}
            .let { call.respond(HttpStatusCode.OK, it) }
    }
    get<RoomMergeApprovesRoutes.RoomMergeApprovesUuid>{response->
        roomMergeApprovesService.getRoomMergeApprovals(response.id)
            ?.toRoomMergeApprovesResponse(roomMergeRequestService, roomService, usersService)
        ?.let { call.respond(HttpStatusCode.OK, it) }?:call.respond(HttpStatusCode.NotFound,"Room merge approve is not found")

    }
    post<RoomMergeApprovesRoutes>{
        val roomMergeApprovesRequest = call.receive<RoomMergeApprovesRequest>()
        roomMergeApprovesService.createRoomMergeApproval(roomMergeApprovesRequest,usersService, roomService, roomMergeRequestService, usecasesService)
            ?.toRoomMergeApprovesResponse(roomMergeRequestService, roomService, usersService)
        ?.let { call.respond(HttpStatusCode.Created, it) } ?: call.respond(HttpStatusCode.BadRequest,"Room merge approve is not found")
    }
    put<RoomMergeApprovesRoutes>{
        val roomMergeApprovesRequest = call.receive<RoomMergeApprovesRequest>()
        roomMergeApprovesService.updateRoomMergeApproval(roomMergeApprovesRequest, usersService, roomMergeRequestService, usecasesService,roomService)
            ?.toRoomMergeApprovesResponse(roomMergeRequestService, roomService, usersService)
        ?.let { call.respond(HttpStatusCode.OK, it) }
    }
    delete<RoomMergeApprovesRoutes.RoomMergeApprovesUuid>{roomMergeApprovesUuid->
        roomMergeApprovesService.deleteRoomMergeApproval(roomMergeApprovesUuid.id)
            .let { call.respond(HttpStatusCode.NoContent) }
    }



}