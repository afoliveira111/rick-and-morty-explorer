package com.example.codetest

import android.app.Application
import com.example.codetest.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CodeTestApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CodeTestApp)
            modules(appModule)
        }
    }
}