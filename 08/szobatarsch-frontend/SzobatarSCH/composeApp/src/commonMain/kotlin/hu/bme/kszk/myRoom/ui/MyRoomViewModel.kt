package hu.bme.kszk.myRoom.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.kszk.service.api.ApiServiceInterface
import hu.bme.kszk.service.api.dto.rooms.RoomRequest
import hu.bme.kszk.service.api.dto.users.UserResponse
import hu.bme.kszk.util.NetworkError
import hu.bme.kszk.util.onError
import hu.bme.kszk.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MyRoomViewModel : ViewModel(), KoinComponent {
    private val _state = MutableStateFlow<MyRoomScreenState>(MyRoomScreenState())
    val state: StateFlow<MyRoomScreenState> = _state.asStateFlow()

    private val apiClient: ApiServiceInterface by inject() // ✅ Proper DI injection


    fun onAction(action: MyRoomScreenAction) {
        when (action) {
            is MyRoomScreenAction.SetUserId -> setUserId(action.userId)
            is MyRoomScreenAction.UpdateRoomName -> updateRoomName(action.roomName)
            is MyRoomScreenAction.UpdateRoomDescription -> updateRoomDescription(action.roomDescription)
            is MyRoomScreenAction.UpdateRoomNumber -> updateRoomNumber(action.roomNumber)
            is MyRoomScreenAction.InviteUser -> inviteUser(action.user)
            else -> Unit
        }
    }


    private fun setUserId(userId: String){
        _state.update { it.copy(
            userId = userId
        ) }
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = true,
                errorMessage= null
            ) }
            state.value.userId?.let { userId ->
                apiClient.getUserRoom(userId)
                    .onSuccess { userRoom ->
                        _state.update { it.copy(
                            room = userRoom
                        ) }
                        apiClient.getOwnRoomScreenData(userId)
                            .onSuccess { ownRoom ->
                                _state.update { it.copy(
                                    members = ownRoom.roomMembers
                                ) }
                                apiClient.getBroingData(userId)
                                    .onSuccess { allUsers ->
                                        _state.update { state ->
                                            state.copy(
                                                invitableUsers = allUsers.bros.filter { user ->( user.userUuid !in state.members.map { it.userUuid }) }
                                            ) }
                                    }
                                    .onError { error ->
                                        _state.update { it.copy(
                                            errorMessage = error
                                        ) }
                                    }
                            }
                            .onError { error ->
                                _state.update { it.copy(
                                    errorMessage = error
                                ) }
                            }
                    }
                    .onError { error ->
                        _state.update { it.copy(
                            errorMessage = error
                        ) }
                    }
            } ?: _state.update { it.copy(errorMessage = NetworkError.MISSING_USER_ID) }

            _state.update { it.copy(
                isLoading = false
            ) }
        }

    }

    private fun updateRoomName(newName: String) {
        _state.update { it.copy(
            room = it.room?.copy(nickname = newName)
        ) }
    }

    private fun updateRoomDescription(roomDescription: String) {
        _state.update { it.copy(
            room = it.room?.copy(roomDescription = roomDescription)
        ) }
    }

    private fun updateRoomNumber(roomNumber: String?) {
        _state.update { it.copy(
            room = it.room?.copy(roomNumber = roomNumber?.toInt())
        ) }
    }


    private fun inviteUser(user: UserResponse) {//TODO
        //viewModelScope.launch {
        //    apiClient.inviteUserToRoom(user.userUuid, _room.value?.roomUUid)
        //}
    }

    fun saveModification(){
        viewModelScope.launch {
            state.value.room?.let {
                RoomRequest(
                    roomUUid = it.roomUUid,
                    roomNumber = it.roomNumber,
                    roomDescription = it.roomDescription,
                    isCoedRoom = it.isCoedRoom,
                    hasLovers = it.hasLovers,
                    isQuiet = it.isQuiet,
                    isSocial = it.isSocial,
                    orientation = it.orientation,
                    nickname = it.nickname,
                    capacity = it.capacity
                )
            }?.let {
                apiClient.updateRoom(
                    it
                )
            }
        }
    }

}
