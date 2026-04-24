package com.calsync.app.di
import android.content.Context
import com.calsync.app.data.local.database.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase = AppDatabase.createInstance(context)

    @Provides @Singleton
    fun provideEventDao(database: AppDatabase): EventDao = database.eventDao()

    @Provides @Singleton
    fun provideTaskDao(database: AppDatabase): TaskDao = database.taskDao()

    @Provides @Singleton
    fun provideCalendarDao(database: AppDatabase): CalendarDao = database.calendarDao()
}
