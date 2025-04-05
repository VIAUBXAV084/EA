package hu.bme.aut.kmp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class CounterEvents{
    data object ResetCounter : CounterEvents()
    data object IncreaseCounter : CounterEvents()
}

data class CounterState(
    val counter : Int = 0
)

class CounterViewModel : ViewModel() {
    private val _stateFlow = MutableStateFlow(CounterState())
    val uiState get() = _stateFlow.asStateFlow()

    fun onEvent(event : CounterEvents){
        when(event){
            is CounterEvents.ResetCounter -> _stateFlow.value = CounterState()
            is CounterEvents.IncreaseCounter -> _stateFlow.value = CounterState(_stateFlow.value.counter + 1)
        }
    }
}