package hu.bme.kszk

import androidx.compose.ui.window.ComposeUIViewController
import hu.bme.kszk.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}