package hu.bme.kszk.service.api

import io.ktor.client.engine.darwin.*

actual fun getHttpClientEngine() = Darwin.create()