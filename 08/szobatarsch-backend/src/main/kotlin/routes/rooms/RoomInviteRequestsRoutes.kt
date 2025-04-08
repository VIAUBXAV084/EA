package hu.bme.kszk.routes.rooms

import hu.bme.kszk.dto.rooms.RoomInviteRequestsRequest
import hu.bme.kszk.dto.rooms.toRoomInviteRequestsResponse
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.rooms.roominviterequest.RoomInviteRequestsServiceInterface
import hu.bme.kszk.service.usecases.UsecasesServiceInterface
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
import java.util.*

@Serializable
@Resource("api/v1/room_invite_requests")
class RoomInviteRequestsRoutes {

    @Resource("id/{id}")
    class RoomInviteRequestUuid(val parent: RoomInviteRequestsRoutes, val id: String)

    @Resource("target_room/{id}")
    class TargetRoomUuid(val parent: RoomInviteRequestsRoutes, val id: String)

    @Resource("inviter/{id}")
    class inviterUuid(val parent: RoomInviteRequestsRoutes, val id: String)

    @Resource("invited/{id}")
    class invitedUuid(val parent: RoomInviteRequestsRoutes, val id: String)
}

fun Route.roomInviteRequestsRoutes(roomInviteRequestsService: RoomInviteRequestsServiceInterface, usersService: UsersServiceInterface, roomsService: RoomsServiceInterface, useCasesService: UsecasesServiceInterface) {

    get<RoomInviteRequestsRoutes> {
        roomInviteRequestsService.getAllRoomInviteRequests().map { roomInviteRequest -> roomInviteRequest.toRoomInviteRequestsResponse(usersService, roomsService) }.let { call.respond(
            HttpStatusCode.OK, it) }
    }

    get<RoomInviteRequestsRoutes.inviterUuid> { inviterUuid ->
        roomInviteRequestsService.getRoomInviteRequestsByInviterId(UUID.fromString(inviterUuid.id)).map { it.toRoomInviteRequestsResponse(usersService, roomsService) }.let {
            if ( it.isEmpty() ) {
                call.respond(HttpStatusCode.NotFound, "The inviter id given is invalid (no such user or inviteRequest exists or format is incorrect)")
            } else call.respond(HttpStatusCode.OK, it)
        }
    }

    get<RoomInviteRequestsRoutes.invitedUuid> { invitedUuid ->
        roomInviteRequestsService.getRoomInviteRequestsByInvitedId(UUID.fromString(invitedUuid.id)).map { it.toRoomInviteRequestsResponse(usersService, roomsService) }.let {
            if ( it.isEmpty() ) {
                call.respond(HttpStatusCode.NotFound, "The invited id given is invalid (no such user or inviteRequest exists or format is incorrect)")
            } else call.respond(HttpStatusCode.OK, it)
        }
    }

    get<RoomInviteRequestsRoutes.TargetRoomUuid> { targetRoomUuid ->
        roomInviteRequestsService.getRoomInviteRequestsByTargetRoomId(UUID.fromString(targetRoomUuid.id))?.toRoomInviteRequestsResponse(usersService, roomsService)?.let {
            call.respond(HttpStatusCode.OK, it)
        } ?: call.respond( HttpStatusCode.NotFound, "The room id given is invalid (no such room exists or format is incorrect)")
    }

    get<RoomInviteRequestsRoutes.RoomInviteRequestUuid> { roomInviteRequestUuid ->
        roomInviteRequestsService.getRoomInviteRequest(roomInviteRequestUuid.id)?.toRoomInviteRequestsResponse(usersService, roomsService)?.let {
            call.respond(HttpStatusCode.OK, it)
        } ?: call.respond( HttpStatusCode.NotFound, "The roomInviteRequest id given is invalid (no such roomInviteRequest exists or format is incorrect)")
    }



    post<RoomInviteRequestsRoutes> {
        val newRoomInviteRequest = call.receive<RoomInviteRequestsRequest>()
        roomInviteRequestsService.createRoomInviteRequest(newRoomInviteRequest, usersService, roomsService, roomInviteRequestsService, useCasesService)?.toRoomInviteRequestsResponse(usersService, roomsService)?.let { call.respond(
            HttpStatusCode.Created, it) } ?: call.respond(HttpStatusCode.InternalServerError, "An error occurred")
    }

    put<RoomInviteRequestsRoutes> {
        val roomInviteRequest = call.receive<RoomInviteRequestsRequest>()
        roomInviteRequestsService.updateRoomInviteRequest(roomInviteRequest, usersService, roomInviteRequestsService, useCasesService, roomsService)?.toRoomInviteRequestsResponse(usersService, roomsService)?.let {
            call.respond(HttpStatusCode.OK, it)
        } ?: call.respond(HttpStatusCode.NotFound, "The provided roomInviteRequest id is invalid")
    }

    delete<RoomInviteRequestsRoutes.RoomInviteRequestUuid> { roomInviteRequestId ->
        roomInviteRequestsService.deleteRoomInviteRequest(roomInviteRequestId.id).let { call.respond(HttpStatusCode.NoContent) }
    }
}