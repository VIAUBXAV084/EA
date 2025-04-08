package hu.bme.kszk.service.users.contactinfos

import hu.bme.kszk.db.users.*
import hu.bme.kszk.dto.users.ContactInfoRequest
import hu.bme.kszk.dto.users.toContactInfoEntity
import hu.bme.kszk.dto.users.updateFromUserRequest
import hu.bme.kszk.service.users.users.UsersServiceInterface
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ContactInfoService : ContactInfoServiceInterface {
    override fun getAllContactInfos(): List<ContactInfoEntity> = transaction {  ContactInfoEntity.all().toList() }

    override fun getContactInfoById(uuid: String): ContactInfoEntity? = transaction { ContactInfoEntity.findById(UUID.fromString(uuid)) }
    override fun getContactInfoById(uuid: UUID): ContactInfoEntity? = transaction { ContactInfoEntity.findById(uuid) }
    override fun getContactInfoByUserId(uuid: String): List<ContactInfoEntity> = transaction {
        ContactInfoEntity.wrapRows(
            UsersTable.innerJoin(ContactInfosTable).selectAll().where{UsersTable.id eq UUID.fromString(uuid)}
        ).toList()
    }

    override fun createContactInfo(createContactInfoRequest: ContactInfoRequest, usersService: UsersServiceInterface): ContactInfoEntity = transaction { createContactInfoRequest.toContactInfoEntity(usersService) }

    override fun updateContactInfo(updateContactInfoRequest: ContactInfoRequest, usersService: UsersServiceInterface): ContactInfoEntity? = transaction { getContactInfoById(updateContactInfoRequest.contactInfoUuid)?.apply { updateFromUserRequest(updateContactInfoRequest, usersService) } }

    override fun deleteContactInfo(uuid: String): Unit = transaction { getContactInfoById(uuid)?.delete() }
}