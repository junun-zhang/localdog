package com.example.ireader

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * 应用程序入口点，使用 Hilt 进行依赖注入
 */
@HiltAndroidApp
class IReaderApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // 应用程序初始化逻辑
    }
}