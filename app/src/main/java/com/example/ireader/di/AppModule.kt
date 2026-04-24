package com.example.ireader.di

import android.content.Context
import android.app.Application
import com.example.ireader.parser.TxtParser
import com.example.ireader.reader.engine.ReadingEngine
import com.example.ireader.reader.engine.TxtReader
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
    fun provideContext(app: Application): Context {
        return app.applicationContext
    }

    @Provides
    @Singleton
    fun provideTxtParser(): TxtParser {
        return TxtParser()
    }

    @Provides
    @Singleton
    fun provideReadingEngine(txtParser: TxtParser): ReadingEngine {
        return TxtReader(txtParser)
    }
}
