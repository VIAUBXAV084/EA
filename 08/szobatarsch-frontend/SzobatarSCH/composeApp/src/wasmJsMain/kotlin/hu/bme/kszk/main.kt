package hu.bme.kszk

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import hu.bme.kszk.di.initKoin
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    println("Koin initialized for WebAssembly!")
    ComposeViewport(document.body!!) {
        App()
    }
}