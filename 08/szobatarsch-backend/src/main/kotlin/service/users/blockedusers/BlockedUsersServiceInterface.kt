package hu.bme.kszk.service.users.blockedusers

import hu.bme.kszk.db.users.BlockedUserEntity
import hu.bme.kszk.dto.users.BlockedUserRequest
import hu.bme.kszk.service.users.userreports.UserReportsServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import java.util.*

interface BlockedUsersServiceInterface {
    fun getAllBlockedUsers(): List<BlockedUserEntity>
    fun getBlockedUserById(uuid: String): BlockedUserEntity?
    fun getBlockedUserById(uuid: UUID): BlockedUserEntity?
    fun getBlockedUserByUserId(uuid: String): BlockedUserEntity?
    fun getIsBlockedByUserId(uuid: String): Boolean
    fun createBlockedUser(createBlockedUserRequest: BlockedUserRequest, usersService: UsersServiceInterface, userReportsService: UserReportsServiceInterface): BlockedUserEntity
    fun updateBlockedUser(updateBlockedUserRequest: BlockedUserRequest, usersService: UsersServiceInterface, userReportsService: UserReportsServiceInterface): BlockedUserEntity?
    fun deleteBlockedUser(uuid: String): Unit
}