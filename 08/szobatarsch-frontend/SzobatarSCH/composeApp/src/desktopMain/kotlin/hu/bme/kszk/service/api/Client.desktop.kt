package hu.bme.kszk.service.api

import io.ktor.client.engine.okhttp.OkHttp

actual fun getHttpClientEngine() = OkHttp.create()