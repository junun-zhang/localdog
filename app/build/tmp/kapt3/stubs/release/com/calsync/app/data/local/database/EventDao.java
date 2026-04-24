package com.calsync.app.data.local.database;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u000b\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u001c\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\r0\f2\u0006\u0010\u000e\u001a\u00020\tH\'J\u0018\u0010\u000f\u001a\u0004\u0018\u00010\u00052\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ,\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\r0\f2\u0006\u0010\u000e\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\'J\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00050\rH\u00a7@\u00a2\u0006\u0002\u0010\u0015J\u0016\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0017\u001a\u00020\u00032\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00050\rH\u00a7@\u00a2\u0006\u0002\u0010\u0019J\u001c\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\r0\f2\u0006\u0010\u001b\u001a\u00020\tH\'J\u0016\u0010\u001c\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u001d"}, d2 = {"Lcom/calsync/app/data/local/database/EventDao;", "", "deleteEvent", "", "event", "Lcom/calsync/app/data/local/entity/EventEntity;", "(Lcom/calsync/app/data/local/entity/EventEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteEventById", "id", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllEvents", "Lkotlinx/coroutines/flow/Flow;", "", "calendarId", "getEventById", "getEventsInRange", "start", "", "end", "getUnsyncedEvents", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertEvent", "insertEvents", "events", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchEvents", "query", "updateEvent", "app_release"})
@androidx.room.Dao()
public abstract interface EventDao {
    
    @androidx.room.Query(value = "SELECT * FROM events WHERE calendarId = :calendarId AND startTime BETWEEN :start AND :end ORDER BY startTime")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.calsync.app.data.local.entity.EventEntity>> getEventsInRange(@org.jetbrains.annotations.NotNull()
    java.lang.String calendarId, long start, long end);
    
    @androidx.room.Query(value = "SELECT * FROM events WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEventById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.calsync.app.data.local.entity.EventEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM events WHERE calendarId = :calendarId ORDER BY startTime DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.calsync.app.data.local.entity.EventEntity>> getAllEvents(@org.jetbrains.annotations.NotNull()
    java.lang.String calendarId);
    
    @androidx.room.Query(value = "SELECT * FROM events WHERE syncStatus != 0")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getUnsyncedEvents(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.calsync.app.data.local.entity.EventEntity>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertEvent(@org.jetbrains.annotations.NotNull()
    com.calsync.app.data.local.entity.EventEntity event, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertEvents(@org.jetbrains.annotations.NotNull()
    java.util.List<com.calsync.app.data.local.entity.EventEntity> events, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateEvent(@org.jetbrains.annotations.NotNull()
    com.calsync.app.data.local.entity.EventEntity event, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteEvent(@org.jetbrains.annotations.NotNull()
    com.calsync.app.data.local.entity.EventEntity event, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM events WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteEventById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM events WHERE title LIKE \'%\' || :query || \'%\' OR description LIKE \'%\' || :query || \'%\'")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.calsync.app.data.local.entity.EventEntity>> searchEvents(@org.jetbrains.annotations.NotNull()
    java.lang.String query);
}