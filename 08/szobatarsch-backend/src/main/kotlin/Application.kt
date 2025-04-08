package hu.bme.kszk

import hu.bme.kszk.db.DatabaseFactory
import hu.bme.kszk.db.configureDatabases
import hu.bme.kszk.plugins.configureHTTP
import hu.bme.kszk.plugins.configureRouting
import hu.bme.kszk.plugins.configureSecurity
import hu.bme.kszk.plugins.configureSerialization
import hu.bme.kszk.service.MainService
import io.ktor.server.application.*


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureDatabases(DatabaseFactory)
    configureHTTP()
    configureSecurity(MainService.usersService, MainService.roomsService)
    configureRouting(MainService)
}
