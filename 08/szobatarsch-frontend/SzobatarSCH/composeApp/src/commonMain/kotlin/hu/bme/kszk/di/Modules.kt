package hu.bme.kszk.di

import hu.bme.kszk.discover.ui.DiscoverViewModel
import hu.bme.kszk.myRoom.ui.MyRoomViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    viewModelOf(::DiscoverViewModel)
    viewModelOf(::MyRoomViewModel)
}