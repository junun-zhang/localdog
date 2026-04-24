package com.example.ireader

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class IReaderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 初始化日志
        Timber.plant(Timber.DebugTree())
    }
}
