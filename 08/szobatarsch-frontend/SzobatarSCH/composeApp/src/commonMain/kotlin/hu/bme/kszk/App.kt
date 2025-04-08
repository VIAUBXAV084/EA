package hu.bme.kszk

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import hu.bme.kszk.screen.NavComponent
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Composable
@Preview
fun App() {
    MaterialTheme {
        KoinContext {
            NavComponent()
        }
    }
}