package hu.bme.kszk.service.users.userpreferences

import hu.bme.kszk.db.users.UserPreferencesEntity
import hu.bme.kszk.dto.users.UserPreferenceRequest
import hu.bme.kszk.service.users.users.UsersServiceInterface
import java.util.*

interface UserPreferencesServiceInterface {
    fun getAllUserPreferences(): List<UserPreferencesEntity>
    fun getUserPreferenceById(uuid: String): UserPreferencesEntity?
    fun getUserPreferenceById(uuid: UUID): UserPreferencesEntity?
    fun getUserPreferenceByUserId(uuid: String): UserPreferencesEntity?
    fun createUserPreference(
        createUserPreferenceRequest: UserPreferenceRequest,
        usersService: UsersServiceInterface
    ): UserPreferencesEntity
    fun updateUserPreference(
        updateUserPreferenceRequest: UserPreferenceRequest,
        usersService: UsersServiceInterface
    ): UserPreferencesEntity?
    fun deleteUserPreference(uuid: String): Unit
}