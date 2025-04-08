package hu.bme.kszk.service.users.bros

import hu.bme.kszk.db.users.BrosEntity
import hu.bme.kszk.db.users.BrosTable
import hu.bme.kszk.db.users.UsersTable
import hu.bme.kszk.dto.users.BroState
import hu.bme.kszk.dto.users.BrosRequest
import hu.bme.kszk.dto.users.toBrosEntity
import hu.bme.kszk.dto.users.updateFromBroRequest
import hu.bme.kszk.service.users.users.UsersServiceInterface
import hu.bme.kszk.szobatarsch.enums.BroStates
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class BroService : BroServiceInterface {
    override fun getAllBros(): List<BrosEntity> = transaction { BrosEntity.all().toList() }

    override fun getBroById(uuid: String): BrosEntity? = transaction { BrosEntity.findById(UUID.fromString(uuid)) }

    override fun getBroById(uuid: UUID): BrosEntity? = transaction { BrosEntity.findById(uuid) }

    //User is the inviter, gives back all invited/target bro
    override fun getBroByInviterUserId(uuid: String, state: BroState): List<BrosEntity> = transaction {
        BrosEntity.wrapRows(
            BrosTable
                .join(UsersTable, JoinType.INNER, additionalConstraint = { BrosTable.inviterUser eq UsersTable.id })
                .selectAll()
                .where { UsersTable.id eq UUID.fromString(uuid) }
        ).toList().filter { bro ->
            state.state?.let{ bro.state.toString() == state.state }?: true
        }
    }

    //User is the target/invited, gives back all inviter bro
    override fun getBroByTargetUserId(uuid: String, state: BroState): List<BrosEntity> = transaction {
        BrosEntity.wrapRows(
            BrosTable
                .join(UsersTable, JoinType.INNER, additionalConstraint = { BrosTable.targetUser eq UsersTable.id })
                .selectAll()
                .where { UsersTable.id eq UUID.fromString(uuid) }
        ).toList().filter { bro ->
            state.state?.let{ bro.state.toString() == state.state }?: true
        }
    }

    override fun createBro(createBroRequest: BrosRequest, usersService: UsersServiceInterface): BrosEntity = transaction { createBroRequest.toBrosEntity(usersService) }

    override fun updateBro(updateBroRequest: BrosRequest, usersService: UsersServiceInterface): BrosEntity? = transaction {getBroById(updateBroRequest.broUuid)?.apply{ updateFromBroRequest(updateBroRequest, usersService) } }

    override fun deleteBro(uuid: String): Unit = transaction { getBroById(uuid)?.delete() }
}