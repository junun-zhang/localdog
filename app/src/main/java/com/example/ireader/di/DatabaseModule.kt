package com.example.ireader.di

import android.content.Context
import androidx.room.Room
import com.example.ireader.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ireader_database"
        ).build()
    }

    @Provides
    fun provideBookDao(db: AppDatabase) = db.bookDao()

    @Provides
    fun provideBookmarkDao(db: AppDatabase) = db.bookmarkDao()

    @Provides
    fun provideAnnotationDao(db: AppDatabase) = db.annotationDao()

    @Provides
    fun provideUserSettingsDao(db: AppDatabase) = db.userSettingsDao()
}
