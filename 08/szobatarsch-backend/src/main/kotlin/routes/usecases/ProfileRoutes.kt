package hu.bme.kszk.routes.usecases


import io.ktor.resources.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import hu.bme.kszk.dto.usecases.ProfileResponse
import hu.bme.kszk.dto.users.toContactInfoResponse
import hu.bme.kszk.dto.users.toUserPreferenceResponse
import hu.bme.kszk.dto.users.toUserResponse
import hu.bme.kszk.service.users.contactinfos.ContactInfoServiceInterface
import hu.bme.kszk.service.users.userpreferences.UserPreferencesService
import hu.bme.kszk.service.users.users.UsersServiceInterface
import io.ktor.http.*
import io.ktor.server.resources.*
import io.ktor.server.response.*

@Serializable
@Resource("api/v1/profile/{id}")
class ProfileRoutes(val id: String)

fun Route.profileRoutes(usersService: UsersServiceInterface, preferencesService: UserPreferencesService, contactInfoService: ContactInfoServiceInterface) {
    //TODO getAll userInfo(user) + contact info
    //TODO How to handle the UserPreferences rooms only where your rom can fit in
    get<ProfileRoutes> { user ->
        val userEntity = usersService.getUserById(user.id)
        val preferences = preferencesService.getUserPreferenceByUserId(user.id)
        val contacts = contactInfoService.getContactInfoByUserId(user.id)
        call.respond(HttpStatusCode.OK, ProfileResponse(userEntity?.toUserResponse(),contacts.map { it.toContactInfoResponse(usersService) }, preferences?.toUserPreferenceResponse(usersService) ))
    }
}