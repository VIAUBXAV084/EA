package hu.bme.kszk.routes.users

import hu.bme.kszk.dto.users.*
import hu.bme.kszk.service.users.contactinfos.ContactInfoServiceInterface
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
@Resource("api/v1/contacts")
class ContactInfoRoutes {

    @Resource("id/{id}")
    class ContactInfoUuid(val parent: ContactInfoRoutes = ContactInfoRoutes(), val id: String)

    @Resource("user/{id}")
    class ContactInfoUserUuid(val parent: ContactInfoRoutes = ContactInfoRoutes(), val id: String)
}

fun Route.contactInfoRoutes(contactInfoService: ContactInfoServiceInterface, userService: UsersServiceInterface) {

    get<ContactInfoRoutes> {
        //var contactInfos: List<ContactInfoResponse>? = null
        contactInfoService.getAllContactInfos().map { it.toContactInfoResponse(userService) }.let { call.respond(HttpStatusCode.OK, it) }

        //TODO paging?
        //val contactInfos = contactInfoDAO.getContactInfos(2, it.page) //Magic number :D Legacy
        // Ensure contactInfos is non-null before responding
    }

    get<ContactInfoRoutes.ContactInfoUuid> { contactInfoRoute ->
        contactInfoService.getContactInfoById(contactInfoRoute.id)?.toContactInfoResponse(userService)?.let {
            call.respond(HttpStatusCode.OK, it) // Return contactInfo in response
        } ?: call.respond( HttpStatusCode.NotFound, "The contactInfo id given is invalid (no such contactInfo exists or format is incorrect)")
    }

    get<ContactInfoRoutes.ContactInfoUserUuid> { contactInfoRoute ->
        contactInfoService.getContactInfoByUserId(contactInfoRoute.id).map{ it.toContactInfoResponse(userService) }.let {
            call.respond(HttpStatusCode.OK, it) // Return contactInfo in response
        } ?: call.respond( HttpStatusCode.NotFound, "The contactInfo id given is invalid (no such contactInfo exists or format is incorrect)")
    }


    post<ContactInfoRoutes> {
        val newContactInfo = call.receive<ContactInfoRequest>()
        contactInfoService.createContactInfo(newContactInfo, userService).toContactInfoResponse(userService).let { call.respond(HttpStatusCode.Created, it) }
    }

    put<ContactInfoRoutes> {
        val contactInfo = call.receive<ContactInfoRequest>()
        contactInfoService.updateContactInfo(contactInfo, userService)?.toContactInfoResponse(userService)?.let {
            call.respond(HttpStatusCode.OK, it)
        } ?: call.respond(HttpStatusCode.NotFound, "The contactInfo id given is invalid")
    }

    delete<ContactInfoRoutes.ContactInfoUuid> { contactInfoId ->
        contactInfoService.deleteContactInfo(contactInfoId.id).let { call.respond(HttpStatusCode.NoContent) }
    }
}