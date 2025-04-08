package hu.bme.kszk

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import hu.bme.kszk.di.initKoin

fun main()  {
    initKoin() // ✅ Must be called BEFORE using DI
    println("Koin initialized for Desktop!")

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Szobatarsch",
        ) {
            App()
        }
    }
}