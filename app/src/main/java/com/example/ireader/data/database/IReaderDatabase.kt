package com.example.ireader.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.ireader.data.model.Book
import com.example.ireader.data.model.Bookmark
import com.example.ireader.data.model.Highlight
import com.example.ireader.data.model.Note

/**
 * IReader 应用的 Room 数据库
 */
@Database(
    entities = [Book::class, Bookmark::class, Highlight::class, Note::class],
    version = 2,
    exportSchema = false
)
abstract class IReaderDatabase : RoomDatabase() {
    
    abstract fun bookDao(): BookDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun highlightDao(): HighlightDao  
    abstract fun noteDao(): NoteDao
    
    companion object {
        @Volatile
        private var INSTANCE: IReaderDatabase? = null
        
        fun getDatabase(context: Context): IReaderDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    IReaderDatabase::class.java,
                    "ireader_database"
                )
                .fallbackToDestructiveMigration() // 临时方案，实际项目中应该使用 Migration
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}