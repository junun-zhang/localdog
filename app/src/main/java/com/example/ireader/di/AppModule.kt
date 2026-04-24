package com.example.ireader.di

import android.content.Context
import com.example.ireader.IReaderApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(application: IReaderApplication): Context {
        return application.applicationContext
    }
}
