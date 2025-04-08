package hu.bme.kszk.service.users.blockedusers


import hu.bme.kszk.db.users.BlockedUserEntity
import hu.bme.kszk.db.users.BlockedUsersTable
import hu.bme.kszk.db.users.UsersTable
import hu.bme.kszk.dto.users.BlockedUserRequest
import hu.bme.kszk.dto.users.toBlockedUserEntity
import hu.bme.kszk.dto.users.updateFromBlockedUserRequest
import hu.bme.kszk.service.users.userreports.UserReportsServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class BlockedUsersService : BlockedUsersServiceInterface {
    override fun getAllBlockedUsers(): List<BlockedUserEntity> =
        transaction { BlockedUserEntity.all().toList() }

    override fun getBlockedUserById(uuid: String): BlockedUserEntity? =
        transaction { BlockedUserEntity.findById(UUID.fromString(uuid)) }

    override fun getBlockedUserById(uuid: UUID): BlockedUserEntity? =
        transaction { BlockedUserEntity.findById(uuid) }

    override fun getBlockedUserByUserId(uuid: String): BlockedUserEntity? = transaction {
        BlockedUserEntity.wrapRows(
            UsersTable.innerJoin(BlockedUsersTable).selectAll().where { UsersTable.id eq UUID.fromString(uuid) }
        ).firstOrNull()
    }

    override fun getIsBlockedByUserId(uuid: String): Boolean = transaction {
        BlockedUserEntity.wrapRows(
            UsersTable.innerJoin(BlockedUsersTable).selectAll().where { UsersTable.id eq UUID.fromString(uuid) }
        ).firstOrNull()?.let{true}?:false
    }

    override fun createBlockedUser(
        createBlockedUserRequest: BlockedUserRequest,
        usersService: UsersServiceInterface,
        userReportsService: UserReportsServiceInterface
    ): BlockedUserEntity = transaction {
        createBlockedUserRequest.toBlockedUserEntity(usersService, userReportsService)
    }

    override fun updateBlockedUser(
        updateBlockedUserRequest: BlockedUserRequest,
        usersService: UsersServiceInterface,
        userReportsService: UserReportsServiceInterface
    ): BlockedUserEntity? = transaction {
        getBlockedUserById(updateBlockedUserRequest.blockedUserUuid)?.apply { updateFromBlockedUserRequest(updateBlockedUserRequest, usersService, userReportsService) }
    }

    override fun deleteBlockedUser(uuid: String) {
        transaction {
            getBlockedUserById(uuid)?.delete()
        }
    }
}