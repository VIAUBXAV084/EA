package hu.bme.kszk.myRoom.ui

import hu.bme.kszk.service.api.dto.users.UserResponse

interface MyRoomScreenAction {
    data class SetUserId(val userId: String) : MyRoomScreenAction
    data class UpdateRoomName(val roomName: String) : MyRoomScreenAction
    data class UpdateRoomDescription(val roomDescription: String) : MyRoomScreenAction
    data class UpdateRoomNumber(val roomNumber: String?) : MyRoomScreenAction
    data class InviteUser(val user: UserResponse) : MyRoomScreenAction
    data object GoBack : MyRoomScreenAction
}