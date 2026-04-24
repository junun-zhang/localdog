package com.calsync.app
import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CalSyncApplication : Application() {
    override fun onCreate() { super.onCreate() }
}
