package hu.bme.kszk.service.users.admins

import hu.bme.kszk.db.users.AdminsEntity
import hu.bme.kszk.db.users.AdminsTable
import hu.bme.kszk.db.users.UsersEntity
import hu.bme.kszk.db.users.UsersTable
import hu.bme.kszk.dto.users.*
import hu.bme.kszk.service.users.users.UsersServiceInterface
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*



class AdminService:AdminsServiceInterface {
    override fun getAllAdmins(): List<AdminsEntity> = transaction { AdminsEntity.all().toList()}
    override fun getAdminById(uuid: String): AdminsEntity? = transaction { AdminsEntity.findById(UUID.fromString(uuid))}
    override fun getAdminById(uuid: UUID): AdminsEntity? = transaction { AdminsEntity.findById(uuid)}
    //finds the admins with matching user(who's got the matching name)

    override fun getAdminByUsername(username: String): AdminsEntity? = transaction {
        AdminsEntity.wrapRows(
            UsersTable.innerJoin(AdminsTable).selectAll().where{UsersTable.name eq username}
        ).firstOrNull()
    }
    override fun isAdminByUsername(username: String): Boolean = transaction {
        AdminsEntity.wrapRows(
            UsersTable.innerJoin(AdminsTable).selectAll().where{UsersTable.name eq username}
        ).firstOrNull()?.let{true}?:false
    }

    override fun createAdmin(createAdminRequest: AdminRequest,usersService: UsersServiceInterface): AdminsEntity= transaction { createAdminRequest.toAdminsEntity(usersService)}
    override fun updateAdmin(updateAdminRequest: AdminRequest,userService:UsersServiceInterface): AdminsEntity? = transaction { getAdminById(updateAdminRequest.adminUuid)?.apply{updateFromAdminRequest(updateAdminRequest, userService)}}
    override fun deleteAdmin(uuid: String): Unit?= transaction { getAdminById(uuid)?.delete() }
}