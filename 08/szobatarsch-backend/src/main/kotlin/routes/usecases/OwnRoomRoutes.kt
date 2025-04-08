package hu.bme.kszk.routes.usecases

import hu.bme.kszk.db.users.UsersEntity
import hu.bme.kszk.dto.rooms.RoomResponse
import hu.bme.kszk.dto.rooms.toRoomResponse
import hu.bme.kszk.dto.usecases.BroingResponse
import hu.bme.kszk.dto.usecases.OwnRoomResponse
import hu.bme.kszk.dto.users.UserResponse
import hu.bme.kszk.dto.users.toUserResponse
import hu.bme.kszk.service.rooms.hidden.HiddenRoomServiceInterface
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.usecases.UsecasesServiceInterface
import hu.bme.kszk.service.users.bros.BroServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


import kotlinx.serialization.Serializable


@Serializable
@Resource("api/v1/ownroom/{id}")
class OwnRoomRoutes(val id: String)

fun Route.ownRoomRoutes(roomsService: RoomsServiceInterface, usersService: UsersServiceInterface, usecasesService: UsecasesServiceInterface, hiddenRoomService: HiddenRoomServiceInterface) {
    //TODO getAll room membres (users with the same roomUuid)
    get<OwnRoomRoutes> { user ->
        val roomMates: MutableList<UserResponse> = mutableListOf()
        val compatibleRooms: MutableList<RoomResponse> = mutableListOf()

        roomMates.addAll(usecasesService.getRoomMembers(user.id, usersService).map { it.toUserResponse() })
        compatibleRooms.addAll(usecasesService.getCompatibleRooms(user.id, roomsService, usersService, hiddenRoomService).map { it.toRoomResponse() })
        call.respond(HttpStatusCode.OK, OwnRoomResponse(roomMates, compatibleRooms))
    }
    //TODO getCompatibleRooms: HOW?
}