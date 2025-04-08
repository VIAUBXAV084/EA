package hu.bme.kszk.db.users

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.*


object AdminsTable : UUIDTable("Admins") {
    val user = reference("user", UsersTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)

}

class AdminsEntity(uuid: EntityID<UUID>) : UUIDEntity(uuid) {
    companion object : UUIDEntityClass<AdminsEntity>(AdminsTable)

    var user by UsersEntity referencedOn AdminsTable.user
}