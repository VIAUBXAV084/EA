package hu.bme.kszk.service.users.admins

import hu.bme.kszk.db.users.AdminsEntity
import hu.bme.kszk.dto.users.AdminRequest
import hu.bme.kszk.service.users.users.UsersServiceInterface
import java.util.*

interface AdminsServiceInterface {
    fun getAllAdmins():List<AdminsEntity>
    fun getAdminById(uuid: String):AdminsEntity?
    fun getAdminById(uuid: UUID): AdminsEntity?
    fun getAdminByUsername(username: String):AdminsEntity?
    fun isAdminByUsername(username: String): Boolean
    fun createAdmin(createAdminRequest: AdminRequest,usersService: UsersServiceInterface): AdminsEntity
    fun updateAdmin(updateAdminRequest: AdminRequest,userService:UsersServiceInterface): AdminsEntity?
    fun deleteAdmin(uuid:String): Unit?
}