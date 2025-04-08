package hu.bme.kszk.service.users.contactinfos

import hu.bme.kszk.db.users.ContactInfoEntity
import hu.bme.kszk.dto.users.ContactInfoRequest
import hu.bme.kszk.dto.users.ContactInfoResponse
import hu.bme.kszk.service.users.users.UsersServiceInterface
import java.util.*


interface ContactInfoServiceInterface {
    fun getAllContactInfos() : List<ContactInfoEntity>
    fun getContactInfoById(uuid: String) : ContactInfoEntity?
    fun getContactInfoById(uuid: UUID): ContactInfoEntity?
    fun getContactInfoByUserId(uuid: String): List<ContactInfoEntity>
    fun createContactInfo(createContactInfoRequest: ContactInfoRequest, usersService: UsersServiceInterface) : ContactInfoEntity
    fun updateContactInfo(updateContactInfoRequest: ContactInfoRequest, usersService: UsersServiceInterface) : ContactInfoEntity?
    fun deleteContactInfo(uuid: String) : Unit
}
