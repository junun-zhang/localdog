package com.example.ireader.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ireader.data.model.Book
import com.example.ireader.data.model.Bookmark
import com.example.ireader.data.model.Highlight
import com.example.ireader.data.model.Note

/**
 * IReader 应用的 Room 数据库
 */
@Database(
    entities = [Book::class, Bookmark::class, Highlight::class, Note::class],
    version = 3,  // 增加版本号
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
        
        // 从版本2迁移到版本3，添加新的列
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 添加新列
                database.execSQL("ALTER TABLE books ADD COLUMN lastReadChapter INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE books ADD COLUMN lastReadMode TEXT NOT NULL DEFAULT 'PAGED'")
                database.execSQL("ALTER TABLE books ADD COLUMN lastScrollPosition INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE books ADD COLUMN lastFontSize INTEGER NOT NULL DEFAULT 16")
                database.execSQL("ALTER TABLE books ADD COLUMN lastZoom REAL NOT NULL DEFAULT 1.0")
            }
        }
        
        fun getDatabase(context: Context): IReaderDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    IReaderDatabase::class.java,
                    "ireader_database"
                )
                .addMigrations(MIGRATION_2_3)  // 添加迁移
                .fallbackToDestructiveMigration() // 如果迁移失败则重建数据库
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}