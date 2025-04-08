package hu.bme.kszk.routes.usecases

import hu.bme.kszk.db.users.UsersEntity
import hu.bme.kszk.dto.usecases.BroingResponse
import hu.bme.kszk.dto.users.toUserResponse
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
@Resource("api/v1/broing/{id}")
class BroingRoutes(val id: String)

fun Route.broingRoutes(brosService: BroServiceInterface, usersService: UsersServiceInterface, usecasesService: UsecasesServiceInterface) {
    get<BroingRoutes>{ user ->
        val myBros: MutableList<UsersEntity> = mutableListOf()
        val requests: MutableList<UsersEntity> = mutableListOf()
        val others: MutableList<UsersEntity> = mutableListOf()

        myBros.addAll(usecasesService.getMyBros(user.id, brosService))
        requests.addAll(usecasesService.getUserRequests(user.id, brosService))
        others.addAll(usecasesService.getOthers(user.id, brosService, usersService))

        call.respond(HttpStatusCode.OK, BroingResponse(myBros.map { it.toUserResponse() }, requests.map { it.toUserResponse() }, others.map { it.toUserResponse() }))
    }

}