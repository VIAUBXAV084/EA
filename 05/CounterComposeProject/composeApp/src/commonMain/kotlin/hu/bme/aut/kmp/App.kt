package hu.bme.aut.kmp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    viewModel: CounterViewModel = viewModel { CounterViewModel() }
) {
    MaterialTheme {
        Scaffold {
            Column(Modifier.fillMaxSize().padding(it), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Button(onClick = { viewModel.onEvent(CounterEvents.IncreaseCounter) }) {
                    Text("Increase Counter")
                }
                Button(onClick = { viewModel.onEvent(CounterEvents.ResetCounter) }) {
                    Text("Reset Counter")
                }
                Box {
                    val currentState by viewModel.uiState.collectAsState()
                    Text("Current count value is : ${currentState.counter}")
                }
            }
        }
    }
}