package com.example.ireader

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Main Application class for IReader
 * Hilt will generate the dependency injection code
 */
@HiltAndroidApp
class IReaderApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Initialize any application-wide components here
    }
}