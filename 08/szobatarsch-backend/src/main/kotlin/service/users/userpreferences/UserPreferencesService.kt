package hu.bme.kszk.service.users.userpreferences

import hu.bme.kszk.db.users.*
import hu.bme.kszk.dto.users.UserPreferenceRequest
import hu.bme.kszk.dto.users.toUserPreferenceEntity
import hu.bme.kszk.dto.users.updateFromUserPreferenceRequest
import hu.bme.kszk.dto.users.updateFromUserRequest
import hu.bme.kszk.service.users.users.UsersServiceInterface
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class UserPreferencesService : UserPreferencesServiceInterface {
    override fun getAllUserPreferences(): List<UserPreferencesEntity> = transaction { UserPreferencesEntity.all().toList() }

    override fun getUserPreferenceById(uuid: String): UserPreferencesEntity? = transaction { UserPreferencesEntity.findById(UUID.fromString(uuid)) }

    override fun getUserPreferenceById(uuid: UUID): UserPreferencesEntity? = transaction { UserPreferencesEntity.findById(uuid) }
    override fun getUserPreferenceByUserId(uuid: String): UserPreferencesEntity? = transaction {
        UserPreferencesEntity.wrapRows(
            UsersTable.innerJoin(UserPreferencesTable).selectAll().where{ UsersTable.id eq UUID.fromString(uuid)}
        ).firstOrNull()
    }

    override fun createUserPreference(
        createUserPreferenceRequest: UserPreferenceRequest,
        usersService: UsersServiceInterface
    ): UserPreferencesEntity = transaction { createUserPreferenceRequest.toUserPreferenceEntity(usersService) }

    override fun updateUserPreference(
        updateUserPreferenceRequest: UserPreferenceRequest,
        usersService: UsersServiceInterface
    ): UserPreferencesEntity? = transaction {
        getUserPreferenceById(updateUserPreferenceRequest.userPreferenceUuid)?.apply {
            updateFromUserPreferenceRequest(updateUserPreferenceRequest, usersService)
        }
    }

    override fun deleteUserPreference(uuid: String): Unit = transaction { getUserPreferenceById(uuid)?.delete() }
}