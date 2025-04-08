package hu.bme.kszk.db.users

import hu.bme.kszk.szobatarsch.enums.ContactType
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.*

object ContactInfosTable : UUIDTable("ContactInfos") {
    val user = reference("user", UsersTable, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val contactType = enumeration("contact_type", ContactType::class)
    val uri = text("uri")
}

class ContactInfoEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ContactInfoEntity>(ContactInfosTable)

    var user by UsersEntity referencedOn ContactInfosTable.user
    var contactType by ContactInfosTable.contactType
    var uri by ContactInfosTable.uri
}