package hu.bme.kszk.service.api.dto.users


import kotlinx.serialization.Serializable


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
