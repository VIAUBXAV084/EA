package hu.bme.kszk.service.api.dto.rooms

import kotlinx.serialization.Serializable

@Serializable
data class BlockedRoomResponse(
    val roomUUid: String,
    val roomNumber: Int,
    val roomDescription: String,
    val isCoedRoom: Boolean,
    val hasLovers: Boolean,
    val isQuiet: Boolean,
    val isSocial: Boolean,
    val orientation: String,
    val nickname: String,
    val capacity: Int
)

@Serializable
data class BlockedRoomRequest(
    val roomUUid: String = "",
    val roomNumber: Int,
    val roomDescription: String,
    val isCoedRoom: Boolean,
    val hasLovers: Boolean,
    val isQuiet: Boolean,
    val isSocial: Boolean,
    val orientation: String,
    val nickname: String,
    val capacity: Int
)