package hu.bme.kszk.service.users.bros

import hu.bme.kszk.db.users.BrosEntity
import hu.bme.kszk.dto.users.BroState
import hu.bme.kszk.dto.users.BrosRequest
import hu.bme.kszk.service.users.users.UsersServiceInterface
import java.util.*

interface BroServiceInterface {
    fun getAllBros() : List<BrosEntity>
    fun getBroById(uuid: String) : BrosEntity?
    fun getBroById(uuid: UUID): BrosEntity?
    fun getBroByInviterUserId(uuid: String, state: BroState): List<BrosEntity>
    fun getBroByTargetUserId(uuid: String, state: BroState): List<BrosEntity>
    fun createBro(createBroRequest: BrosRequest, usersService: UsersServiceInterface) : BrosEntity
    fun updateBro(updateBroRequest: BrosRequest, usersService: UsersServiceInterface) : BrosEntity?
    fun deleteBro(uuid: String) : Unit
}