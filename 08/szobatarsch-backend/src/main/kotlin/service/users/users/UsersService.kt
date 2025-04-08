package hu.bme.kszk.service.users.users

import hu.bme.kszk.db.rooms.RoomsTable
import hu.bme.kszk.db.users.*
import hu.bme.kszk.dto.users.*
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.szobatarsch.enums.BroStates
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.collections.union

class UsersService : UsersServiceInterface {
    override fun getAllUsers(): List<UsersEntity> = transaction { UsersEntity.all().toList() }

    override fun getUserById(uuid: String): UsersEntity? = transaction { UsersEntity.findById(UUID.fromString(uuid)) }
    override fun getUserById(uuid: UUID): UsersEntity? = transaction { UsersEntity.findById(uuid) }
    override fun getUserByAuthSchId(authSchId: String): UsersEntity? = transaction { UsersEntity.find { UsersTable.authSchId eq authSchId }.firstOrNull() }

    override fun getUserBySchacc(schacc: String): UsersEntity? = transaction { UsersEntity.find { UsersTable.schacc eq schacc }.firstOrNull() }
    override fun getUsersByRoom(uuid: String): List<UsersEntity> = transaction {
        UsersEntity.wrapRows(
            RoomsTable.innerJoin(UsersTable).selectAll().where{RoomsTable.id eq UUID.fromString(uuid)}
        ).toList()
    }

    override fun getUsersByBroState(uuid: String): List<UsersEntity> = transaction {
        val userId = UUID.fromString(uuid)

        BrosEntity.find { (BrosTable.targetUser eq userId)  and (BrosTable.state eq BroStates.ACCEPTED)}.map { it.inviterUser }.union(
            BrosEntity.find { (BrosTable.inviterUser eq userId)  and (BrosTable.state eq BroStates.ACCEPTED) }.map { it.targetUser }
        ).toList()

    }


    override fun createUser(createUserRequest: UserRequest, roomsService: RoomsServiceInterface): UsersEntity = transaction { createUserRequest.toUserEntity(roomsService) }

    override fun updateUser(updateUserRequest: UserRequest, roomsService: RoomsServiceInterface): UsersEntity? = transaction { getUserById(updateUserRequest.userUuid)?.apply { updateFromUserRequest(updateUserRequest, roomsService) } }

    override fun deleteUser(uuid: String): Unit  = transaction { getUserById(uuid)?.delete() }
}