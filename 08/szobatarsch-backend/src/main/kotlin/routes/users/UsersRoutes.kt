package hu.bme.kszk.routes.users

import hu.bme.kszk.dto.users.*
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
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
@Resource("api/v1/users")
class UsersRoutes {

    @Resource("id/{id}")
    class UserUuid(val parent: UsersRoutes = UsersRoutes(), val id: String)

    @Resource("schacc/{schacc}")
    class UserSchacc(val parent: UsersRoutes = UsersRoutes(), val schacc: String)

    @Resource("room/{room}")
    class UserRoom(val parent: UsersRoutes = UsersRoutes(), val room: String)

    @Resource("bro/{bro}")
    class UserBro(val parent: UsersRoutes = UsersRoutes(), val bro: String)
}

fun Route.userRoutes(userService: UsersServiceInterface, roomService: RoomsServiceInterface) {

    get<UsersRoutes> {
        //var users: List<UserResponse>? = null
        userService.getAllUsers().map { room -> room.toUserResponse() }.let { call.respond(HttpStatusCode.OK, it) }

        //TODO paging?
        //val users = userDAO.getUsers(2, it.page) //Magic number :D Legacy
        // Ensure users is non-null before responding
    }

    get<UsersRoutes.UserUuid> { userRoute ->
        userService.getUserById(userRoute.id)?.toUserResponse()?.let {
            call.respond(HttpStatusCode.OK, it) // Return user in response
        } ?: call.respond( HttpStatusCode.NotFound, "The user id given is invalid (no such user exists or format is incorrect)")
    }

    get<UsersRoutes.UserSchacc> { userRoute ->
        userService.getUserBySchacc(userRoute.schacc)?.toUserResponse()?.let {
            call.respond(HttpStatusCode.OK, it) // Return user in response
        } ?: call.respond(
            HttpStatusCode.NotFound,
            "The user id given is invalid (no such user exists or format is incorrect)"
        )
    }

    get<UsersRoutes.UserRoom> { userRoute ->
        userService.getUsersByRoom(userRoute.room).map { it.toUserResponse() }.let {
            call.respond(HttpStatusCode.OK, it) // Return user in response
        }
    }

    get<UsersRoutes.UserBro> { userRoute ->
        userService.getUsersByBroState(userRoute.bro).map { it.toUserResponse() }.let {
            call.respond(HttpStatusCode.OK, it) // Return user in response
        }
    }

    post<UsersRoutes> {
        val newUser = call.receive<UserRequest>()
        userService.createUser(newUser, roomService).toUserResponse().let { call.respond(HttpStatusCode.Created, it) }
    }

    put<UsersRoutes> {
        val user = call.receive<UserRequest>()
        userService.updateUser(user, roomService)?.toUserResponse()?.let {
            call.respond(HttpStatusCode.OK, it)
        } ?: call.respond(HttpStatusCode.NotFound, "The user id given is invalid")
    }

    delete<UsersRoutes.UserUuid> { userId ->
        userService.deleteUser(userId.id).let { call.respond(HttpStatusCode.NoContent) }
    }
}