package hu.bme.kszk.routes.users

import hu.bme.kszk.dto.users.BlockedUserRequest
import hu.bme.kszk.dto.users.toBlockedUserResponse
import hu.bme.kszk.service.users.blockedusers.BlockedUsersServiceInterface
import hu.bme.kszk.service.users.userreports.UserReportsServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.resources.delete
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("api/v1/blockedusers")
class BlockedUsersRoutes {

    @Resource("id/{id}")
    class BlockedUserUuid(val parent: BlockedUsersRoutes = BlockedUsersRoutes(), val id: String)

    @Resource("user/{id}")
    class BlockedUserUserUuid(val parent: BlockedUsersRoutes = BlockedUsersRoutes(), val id: String)

    @Resource("isBlocked/{id}")
    class IsBlockedUserUuid(val parent: BlockedUsersRoutes = BlockedUsersRoutes(), val id: String)
}

fun Route.blockedUsersRoutes(
    blockedUsersService: BlockedUsersServiceInterface,
    usersService: UsersServiceInterface,
    userReportsService: UserReportsServiceInterface
) {

    get<BlockedUsersRoutes> {
        blockedUsersService.getAllBlockedUsers()
            .map { it.toBlockedUserResponse(usersService, userReportsService) }
            .let { call.respond(HttpStatusCode.OK, it) }
    }

    get<BlockedUsersRoutes.BlockedUserUuid> { blockedUserRoute ->
        blockedUsersService.getBlockedUserById(blockedUserRoute.id)?.toBlockedUserResponse(usersService, userReportsService)?.let {
            call.respond(HttpStatusCode.OK, it)
        } ?: call.respond(HttpStatusCode.NotFound, "The blocked user ID given is invalid")
    }

    get<BlockedUsersRoutes.BlockedUserUserUuid> { blockedUserRoute ->
        blockedUsersService.getBlockedUserByUserId(blockedUserRoute.id)?.toBlockedUserResponse(usersService, userReportsService)?.let {
            call.respond(HttpStatusCode.OK, it)
        } ?: call.respond(HttpStatusCode.NotFound, "The blocked user ID given is invalid")
    }

    get<BlockedUsersRoutes.IsBlockedUserUuid> { blockedUserRoute ->
        blockedUsersService.getIsBlockedByUserId(blockedUserRoute.id).let {
            call.respond(HttpStatusCode.OK, it)
        }
    }


    post<BlockedUsersRoutes> {
        val newBlockedUser = call.receive<BlockedUserRequest>()
        blockedUsersService.createBlockedUser(newBlockedUser, usersService, userReportsService)
            .toBlockedUserResponse(usersService, userReportsService)
            .let { call.respond(HttpStatusCode.Created, it) }
    }

    put<BlockedUsersRoutes> {
        val blockedUser = call.receive<BlockedUserRequest>()
        blockedUsersService.updateBlockedUser(blockedUser, usersService, userReportsService)
            ?.toBlockedUserResponse(usersService, userReportsService)
            ?.let { call.respond(HttpStatusCode.OK, it) }
            ?: call.respond(HttpStatusCode.NotFound, "The blocked user ID given is invalid")
    }

    delete<BlockedUsersRoutes.BlockedUserUuid> { blockedUserId ->
        blockedUsersService.deleteBlockedUser(blockedUserId.id).let {
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
