package hu.bme.kszk.routes.users

import hu.bme.kszk.dto.users.*
import hu.bme.kszk.service.users.admins.AdminsServiceInterface
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
@Resource("api/v1/admins")
class AdminRoutes {

    @Resource("id/{id}")
    class AdminUuid (val parent: AdminRoutes= AdminRoutes(), val id: String)

    @Resource("name/{name}")
    class AdminName(val parent: AdminRoutes= AdminRoutes(), val name: String)
    @Resource("isName/{name}")
    class AdminIsName(val parent: AdminRoutes= AdminRoutes(), val name: String)
}

fun Route.adminRoutes(adminService: AdminsServiceInterface, userService: UsersServiceInterface) {
    get<AdminRoutes>{
        adminService.getAllAdmins().map{admin->admin.toAdminsResponse() }.let { call.respond(HttpStatusCode.OK, it) }
    }

    get<AdminRoutes.AdminUuid>{adminRoute->
        adminService.getAdminById(adminRoute.id)?.toAdminsResponse()?.let { call.respond(HttpStatusCode.OK, it)
        }?:call.respond(HttpStatusCode.NotFound,"The admin id given is invalid(no such admin exists or format is incorrect)")
    }
    get<AdminRoutes.AdminName>{adminRoute->
        adminService.getAdminByUsername(adminRoute.name)?.toAdminsResponse()?.let { call.respond(HttpStatusCode.OK, it)
        }?:call.respond(HttpStatusCode.NotFound,"No such admin with the name given")
    }
    get<AdminRoutes.AdminIsName>{adminRoute->
       if( adminService.isAdminByUsername(adminRoute.name) ) call.respond(HttpStatusCode.OK,true) else call.respond(HttpStatusCode.NotFound, false)
    }


    post<AdminRoutes> {
        val newAdmin = call.receive<AdminRequest>()
        adminService.createAdmin(newAdmin,userService).toAdminsResponse().let { call.respond(HttpStatusCode.Created, it) }
    }
    put<AdminRoutes>{
        val admin = call.receive<AdminRequest>()
        adminService.updateAdmin(admin,userService)?.toAdminsResponse()?.
        let { call.respond(HttpStatusCode.OK, it) }
            ?:call.respond(HttpStatusCode.NotFound, "The admin id given is invalid(no such admin)")
    }
    delete<AdminRoutes>{adminId->
        adminService.deleteAdmin(adminId.toString())?.let { call.respond(HttpStatusCode.NoContent, it) }
            ?:call.respond(HttpStatusCode.NotFound)
    }
}