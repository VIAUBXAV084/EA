package hu.bme.kszk.common.ui

// Web natív alert()
import androidx.compose.runtime.Composable
import kotlinx.browser.window

@Composable
actual fun ShowToast(message: String) {
    window.alert(message)
}