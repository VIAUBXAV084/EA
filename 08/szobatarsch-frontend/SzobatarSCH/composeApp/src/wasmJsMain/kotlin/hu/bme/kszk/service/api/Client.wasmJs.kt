package hu.bme.kszk.service.api

import io.ktor.client.engine.js.*

actual fun getHttpClientEngine() = Js.create()
