package com.calsync.app.data.local.database;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \t2\u00020\u0001:\u0001\tB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&\u00a8\u0006\n"}, d2 = {"Lcom/calsync/app/data/local/database/AppDatabase;", "Landroidx/room/RoomDatabase;", "()V", "calendarDao", "Lcom/calsync/app/data/local/database/CalendarDao;", "eventDao", "Lcom/calsync/app/data/local/database/EventDao;", "taskDao", "Lcom/calsync/app/data/local/database/TaskDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {com.calsync.app.data.local.entity.EventEntity.class, com.calsync.app.data.local.entity.TaskEntity.class, com.calsync.app.data.local.entity.CalendarEntity.class, com.calsync.app.data.local.entity.UserEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String DATABASE_NAME = "calsync_database";
    @org.jetbrains.annotations.NotNull()
    public static final com.calsync.app.data.local.database.AppDatabase.Companion Companion = null;
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.calsync.app.data.local.database.EventDao eventDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.calsync.app.data.local.database.TaskDao taskDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.calsync.app.data.local.database.CalendarDao calendarDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/calsync/app/data/local/database/AppDatabase$Companion;", "", "()V", "DATABASE_NAME", "", "createInstance", "Lcom/calsync/app/data/local/database/AppDatabase;", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.calsync.app.data.local.database.AppDatabase createInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}