package com.calsync.app.data.local.database
import android.content.Context
import androidx.room.*
import com.calsync.app.data.local.entity.*

@Database(
    entities = [EventEntity::class, TaskEntity::class, CalendarEntity::class, UserEntity::class],
    version = 1, exportSchema = false
)
@TypeConverters(ReminderTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun taskDao(): TaskDao
    abstract fun calendarDao(): CalendarDao

    companion object {
        private const val DATABASE_NAME = "calsync_database"
        fun createInstance(context: Context): AppDatabase =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration().build()
    }
}
