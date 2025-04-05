package hu.bme.aut.kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform