package com.example.ireader

import android.app.Application
import com.example.ireader.util.BookScanner
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class IReaderApplication : Application() {
    @Inject lateinit var bookScanner: BookScanner
    
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
