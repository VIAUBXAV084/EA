package hu.bme.kszk.routes.users

import hu.bme.kszk.dto.users.BroState
import hu.bme.kszk.dto.users.BrosRequest
import hu.bme.kszk.dto.users.toBrosResponse
import hu.bme.kszk.service.users.bros.BroServiceInterface
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
@Resource("api/v1/bros")
class BroRoutes {

    @Resource("id/{id}")
    class BroUuid(val parent: BroRoutes = BroRoutes(), val id: String)

    @Resource("inviter/{id}")
    class InviterUuid(val parent: BroRoutes = BroRoutes(), val id: String)

    @Resource("target/{id}")
    class TargetUuid(val parent: BroRoutes = BroRoutes(), val id: String)
}

fun Route.broRoutes(broService: BroServiceInterface, userService: UsersServiceInterface) {

    get<BroRoutes> {
        //var bros: List<BroResponse>? = null
        broService.getAllBros().map { room -> room.toBrosResponse(userService) }.let { call.respond(HttpStatusCode.OK, it) }

        //TODO paging?
        //val bros = broDAO.getBros(2, it.page) //Magic number :D Legacy
        // Ensure bros is non-null before responding
    }

    get<BroRoutes.BroUuid> { broRoute ->
        broService.getBroById(broRoute.id)?.toBrosResponse(userService)?.let {
            call.respond(HttpStatusCode.OK, it) // Return bro in response
        } ?: call.respond( HttpStatusCode.NotFound, "The bro id given is invalid (no such bro exists or format is incorrect)")
    }

    get<BroRoutes.InviterUuid> { broRoute ->
        val state = call.receive<BroState>()
        broService.getBroByInviterUserId(broRoute.id, state).map { it.toBrosResponse(userService)}.let {
            call.respond(HttpStatusCode.OK, it) // Return bro in response
        }
    }

    get<BroRoutes.TargetUuid> { broRoute ->
        val state = call.receive<BroState>()
        broService.getBroByTargetUserId(broRoute.id, state).map { it.toBrosResponse(userService)}.let {
            call.respond(HttpStatusCode.OK, it) // Return bro in response
        }
    }


    post<BroRoutes> {
        val newBro = call.receive<BrosRequest>()
        broService.createBro(newBro, userService).toBrosResponse(userService).let { call.respond(HttpStatusCode.Created, it) }
    }

    put<BroRoutes> {
        val bro = call.receive<BrosRequest>()
        broService.updateBro(bro, userService)?.toBrosResponse(userService)?.let {
            call.respond(HttpStatusCode.OK, it)
        } ?: call.respond(HttpStatusCode.NotFound, "The bro id given is invalid")
    }

    delete<BroRoutes.BroUuid> { broId ->
        broService.deleteBro(broId.id).let { call.respond(HttpStatusCode.NoContent) }
    }
}