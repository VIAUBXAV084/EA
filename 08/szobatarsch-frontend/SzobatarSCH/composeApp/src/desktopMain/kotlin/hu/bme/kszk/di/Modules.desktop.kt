package hu.bme.kszk.di

import hu.bme.kszk.service.api.ApiServiceImpl
import hu.bme.kszk.service.api.ApiServiceInterface
import hu.bme.kszk.service.api.getHttpClientEngine
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {
    single {ApiServiceImpl(getHttpClientEngine())}.bind<ApiServiceInterface>()
}