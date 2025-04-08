package hu.bme.kszk.dto.users


import hu.bme.kszk.db.users.ContactInfoEntity
import hu.bme.kszk.service.users.users.UsersServiceInterface
import hu.bme.kszk.szobatarsch.enums.ContactType
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction


@Serializable
data class ContactInfoResponse(
    val contactInfoUuid: String,
    val user: UserResponse, //TODO Remove or nullable?
    val contactType: String,
    val uri: String
)


@Serializable
data class ContactInfoRequest(
    val contactInfoUuid: String = "",
    val userUuid: String,
    val contactType: String,
    val uri: String
)


fun ContactInfoRequest.toContactInfoEntity(usersService: UsersServiceInterface): ContactInfoEntity =
    ContactInfoEntity.new {
        user = usersService.getUserById(this@toContactInfoEntity.userUuid)!!
        contactType = ContactType.valueOf(this@toContactInfoEntity.contactType)
        uri = this@toContactInfoEntity.uri
    }


fun ContactInfoEntity.toContactInfoResponse(usersService: UsersServiceInterface): ContactInfoResponse =
    ContactInfoResponse(
        contactInfoUuid = this.id.value.toString(),
        user = transaction { usersService.getUserById(this@toContactInfoResponse.user.id.value)!!.toUserResponse() },
        contactType = this.contactType.toString(),
        uri = this.uri
    )


fun ContactInfoEntity.updateFromUserRequest(contactInfoRequest: ContactInfoRequest, userService: UsersServiceInterface) {
    user = userService.getUserById(contactInfoRequest.userUuid)!!
    contactType =  ContactType.valueOf(contactInfoRequest.contactType)
    uri = contactInfoRequest.uri
}


