package com.example.ireader.data.database

import android.content.Context
import androidx.room.Room
import com.example.ireader.data.repository.BookRepository
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
    fun provideDatabase(@ApplicationContext context: Context): IReaderDatabase {
        return Room.databaseBuilder(
            context,
            IReaderDatabase::class.java,
            "ireader_database"
        ).build()
    }
    
    @Provides
    @Singleton
    fun provideBookDao(database: IReaderDatabase): BookDao {
        return database.bookDao()
    }
    
    @Provides
    @Singleton
    fun provideBookmarkDao(database: IReaderDatabase): BookmarkDao {
        return database.bookmarkDao()
    }
    
    @Provides
    @Singleton
    fun provideNoteDao(database: IReaderDatabase): NoteDao {
        return database.noteDao()
    }
    
    @Provides
    @Singleton
    fun provideHighlightDao(database: IReaderDatabase): HighlightDao {
        return database.highlightDao()
    }
    
    @Provides
    @Singleton
    fun provideBookRepository(@ApplicationContext context: Context): BookRepository {
        return BookRepository.getInstance(context)
    }
}
