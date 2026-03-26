package com.boomingarage.android

import android.app.Application
import com.boomingarage.android.di.androidModule
import com.boomingarage.shared.di.commonModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BoominGarageApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BoominGarageApp)
            modules(commonModule, androidModule)
        }
    }
}
