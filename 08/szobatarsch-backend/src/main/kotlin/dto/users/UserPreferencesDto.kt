package hu.bme.kszk.dto.users

import hu.bme.kszk.db.users.UserPreferencesEntity
import hu.bme.kszk.service.users.users.UsersServiceInterface
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class UserPreferenceResponse(
    val userPreferenceUuid: String,
    val user: UserResponse,
    val lateSleep: Int?,
    val isSociable: Int?,
    val nightGuest: Int?,
    val disturbedSleep: Int?,
    val muchStuff: Int?,
    val longTimeSpentInDorm: Int?,
    val annoyedByRubbish: Int?,
    val requiresFrequentCleaning: Int?,
    val lotOfFridgeSpace: Int?,
    val bigSizeObjects: Int?,
    val topOrBottomBed: Int?,
    val mansNotHot: Int?,
    val smokey: Int?,
    val drinky: Int?
)

@Serializable
data class UserPreferenceRequest(
    val userPreferenceUuid: String = "",
    val userUuid: String,
    val lateSleep: Int?,
    val isSociable: Int?,
    val nightGuest: Int?,
    val disturbedSleep: Int?,
    val muchStuff: Int?,
    val longTimeSpentInDorm: Int?,
    val annoyedByRubbish: Int?,
    val requiresFrequentCleaning: Int?,
    val lotOfFridgeSpace: Int?,
    val bigSizeObjects: Int?,
    val topOrBottomBed: Int?,
    val mansNotHot: Int?,
    val smokey: Int?,
    val drinky: Int?
)

fun UserPreferenceRequest.toUserPreferenceEntity(usersService: UsersServiceInterface): UserPreferencesEntity =
    UserPreferencesEntity.new {
        user = usersService.getUserById(this@toUserPreferenceEntity.userUuid)!!
        lateSleep = this@toUserPreferenceEntity.lateSleep
        isSociable = this@toUserPreferenceEntity.isSociable
        nightGuest = this@toUserPreferenceEntity.nightGuest
        disturbedSleep = this@toUserPreferenceEntity.disturbedSleep
        muchStuff = this@toUserPreferenceEntity.muchStuff
        longTimeSpentInDorm = this@toUserPreferenceEntity.longTimeSpentInDorm
        annoyedByRubbish = this@toUserPreferenceEntity.annoyedByRubbish
        requiresFrequentCleaning = this@toUserPreferenceEntity.requiresFrequentCleaning
        lotOfFridgeSpace = this@toUserPreferenceEntity.lotOfFridgeSpace
        bigSizeObjects = this@toUserPreferenceEntity.bigSizeObjects
        topOrBottomBed = this@toUserPreferenceEntity.topOrBottomBed
        mansNotHot = this@toUserPreferenceEntity.mansNotHot
        smokey = this@toUserPreferenceEntity.smokey
        drinky = this@toUserPreferenceEntity.drinky
    }

fun UserPreferencesEntity.toUserPreferenceResponse(usersService: UsersServiceInterface): UserPreferenceResponse =
    UserPreferenceResponse(
        userPreferenceUuid = this.id.value.toString(),
        user = transaction { usersService.getUserById(this@toUserPreferenceResponse.user.id.value)!!.toUserResponse() },
        lateSleep = this.lateSleep,
        isSociable = this.isSociable,
        nightGuest = this.nightGuest,
        disturbedSleep = this.disturbedSleep,
        muchStuff = this.muchStuff,
        longTimeSpentInDorm = this.longTimeSpentInDorm,
        annoyedByRubbish = this.annoyedByRubbish,
        requiresFrequentCleaning = this.requiresFrequentCleaning,
        lotOfFridgeSpace = this.lotOfFridgeSpace,
        bigSizeObjects = this.bigSizeObjects,
        topOrBottomBed = this.topOrBottomBed,
        mansNotHot = this.mansNotHot,
        smokey = this.smokey,
        drinky = this.drinky
    )

fun UserPreferencesEntity.updateFromUserPreferenceRequest(userPreferenceRequest: UserPreferenceRequest, usersService: UsersServiceInterface) {
    user = usersService.getUserById(userPreferenceRequest.userUuid)!!
    lateSleep = userPreferenceRequest.lateSleep
    isSociable = userPreferenceRequest.isSociable
    nightGuest = userPreferenceRequest.nightGuest
    disturbedSleep = userPreferenceRequest.disturbedSleep
    muchStuff = userPreferenceRequest.muchStuff
    longTimeSpentInDorm = userPreferenceRequest.longTimeSpentInDorm
    annoyedByRubbish = userPreferenceRequest.annoyedByRubbish
    requiresFrequentCleaning = userPreferenceRequest.requiresFrequentCleaning
    lotOfFridgeSpace = userPreferenceRequest.lotOfFridgeSpace
    bigSizeObjects = userPreferenceRequest.bigSizeObjects
    topOrBottomBed = userPreferenceRequest.topOrBottomBed
    mansNotHot = userPreferenceRequest.mansNotHot
    smokey = userPreferenceRequest.smokey
    drinky = userPreferenceRequest.drinky
}
