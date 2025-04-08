package hu.bme.kszk.dto.rooms

import hu.bme.kszk.db.rooms.RoomJoinRequestsEntity
import kotlinx.serialization.Serializable

@Serializable
data class RoomJoinRequestsRequest(
    val roomJoinRequestsUUID: String="",
)

@Serializable
data class RoomJoinRequestsResponse(
    val roomJoinRequestsUUID: String,
)

fun RoomJoinRequestsRequest.toRoomJoinRequestsEntity (

): RoomJoinRequestsEntity = RoomJoinRequestsEntity.new{

}

fun RoomJoinRequestsEntity.toRoomJoinRequestsResponse(

) : RoomJoinRequestsResponse=RoomJoinRequestsResponse(
    roomJoinRequestsUUID = TODO()
)

fun RoomJoinRequestsEntity.updateFromRoomJoinRequestsRequest(

){

}