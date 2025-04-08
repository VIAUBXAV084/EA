package hu.bme.kszk.discover.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.kszk.service.api.ApiServiceInterface
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

class DiscoverViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel(), KoinComponent {
    private val _state = MutableStateFlow<DiscoverScreenState>(DiscoverScreenState())
    val state: StateFlow<DiscoverScreenState> = _state.asStateFlow()

    private val apiClient: ApiServiceInterface by inject() // ✅ Proper DI injection


    fun onAction(action: DiscoverScreenAction) {
        when (action) {
            is DiscoverScreenAction.SetUserId -> setUserId(action.userId)
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
                isLoading= true,
                errorMessage= null
            ) }
            state.value.userId?.let { userId ->
                apiClient.getDiscoverScreenData(userId)
                    .onSuccess { success ->
                        _state.update { it.copy(
                            rooms= success.rooms,
                            users= success.users
                        ) }
                    }
                    .onError { error ->
                        _state.update { it.copy(
                            errorMessage = error,
                        ) }
                    }
            } ?: _state.update { it.copy(errorMessage = NetworkError.MISSING_USER_ID) }
            _state.update { it.copy(
                isLoading = false,
            ) }
        }
    }
}
