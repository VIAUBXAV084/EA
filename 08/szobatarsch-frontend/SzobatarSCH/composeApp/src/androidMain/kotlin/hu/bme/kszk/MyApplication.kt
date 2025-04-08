package hu.bme.kszk

import android.app.Application
import hu.bme.kszk.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MyApplication)
            androidLogger()
        }
    }
}