package com.example.ireader.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.ireader.data.model.Book

/**
 * IReader 应用的 Room 数据库
 */
@Database(
    entities = [Book::class],
    version = 1,
    exportSchema = false
)
abstract class IReaderDatabase : RoomDatabase() {
    
    abstract fun bookDao(): BookDao
    
    companion object {
        @Volatile
        private var INSTANCE: IReaderDatabase? = null
        
        fun getDatabase(context: Context): IReaderDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    IReaderDatabase::class.java,
                    "ireader_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}