package hu.bme.aut.kmp

actual fun getCurrentUrl(): String =
    js("window.location.href")