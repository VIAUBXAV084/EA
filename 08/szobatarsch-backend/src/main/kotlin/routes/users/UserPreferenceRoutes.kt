package hu.bme.kszk.routes.users

import hu.bme.kszk.service.users.userpreferences.UserPreferencesServiceInterface
import hu.bme.kszk.dto.users.*
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
@Resource("api/v1/preferences")
class UserPreferenceRoutes {

    @Resource("id/{id}")
    class UserPreferenceUuid(val parent: UserPreferenceRoutes = UserPreferenceRoutes(), val id: String)

    @Resource("user/{id}")
    class UserPreferenceUserUuid(val parent: UserPreferenceRoutes = UserPreferenceRoutes(), val id: String)

}

fun Route.userPreferenceRoutes(
    userPreferenceService: UserPreferencesServiceInterface,
    userService: UsersServiceInterface
) {

    get<UserPreferenceRoutes> {
        userPreferenceService.getAllUserPreferences().map { it.toUserPreferenceResponse(userService) }.let {
            call.respond(HttpStatusCode.OK, it)
        }
    }

    get<UserPreferenceRoutes.UserPreferenceUuid> { userPreferenceRoute ->
        userPreferenceService.getUserPreferenceById(userPreferenceRoute.id)?.toUserPreferenceResponse(userService)?.let {
            call.respond(HttpStatusCode.OK, it)
        } ?: call.respond(HttpStatusCode.NotFound, "The preference id given is invalid (no such preference exists or format is incorrect)")
    }

    get<UserPreferenceRoutes.UserPreferenceUserUuid> { userPreferenceRoute ->
        userPreferenceService.getUserPreferenceByUserId(userPreferenceRoute.id)?.toUserPreferenceResponse(userService)?.let {
            call.respond(HttpStatusCode.OK, it)
        } ?: call.respond(HttpStatusCode.NotFound, "The preference id given is invalid (no such preference exists or format is incorrect)")
    }

    post<UserPreferenceRoutes> {
        val newUserPreference = call.receive<UserPreferenceRequest>()
        userPreferenceService.createUserPreference(newUserPreference, userService).toUserPreferenceResponse(userService).let {
            call.respond(HttpStatusCode.Created, it)
        }
    }

    put<UserPreferenceRoutes> {
        val userPreference = call.receive<UserPreferenceRequest>()
        userPreferenceService.updateUserPreference(userPreference, userService)?.toUserPreferenceResponse(userService)?.let {
            call.respond(HttpStatusCode.OK, it)
        } ?: call.respond(HttpStatusCode.NotFound, "The preference id given is invalid")
    }

    delete<UserPreferenceRoutes.UserPreferenceUuid> { userPreferenceId ->
        userPreferenceService.deleteUserPreference(userPreferenceId.id).let {
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
