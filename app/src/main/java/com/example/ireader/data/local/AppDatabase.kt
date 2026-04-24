package com.example.ireader.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ireader.data.local.dao.AnnotationDao
import com.example.ireader.data.local.dao.BookDao
import com.example.ireader.data.local.dao.BookmarkDao
import com.example.ireader.data.local.entity.Annotation
import com.example.ireader.data.local.entity.Book
import com.example.ireader.data.local.entity.Bookmark
import com.example.ireader.data.local.entity.ReadingHistory
import com.example.ireader.data.local.entity.UserSettings

@Database(
    entities = [
        Book::class,
        Bookmark::class,
        Annotation::class,
        ReadingHistory::class,
        UserSettings::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun annotationDao(): AnnotationDao
}
