package com.calsync.app.data.repository;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J,\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u000e\u0010\u000fJ$\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\b2\u0006\u0010\u0012\u001a\u00020\u000bH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0013\u0010\u0014J\u0012\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00170\u0016J\u0018\u0010\u0018\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0012\u001a\u00020\u000bH\u0086@\u00a2\u0006\u0002\u0010\u0014J\u0012\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00170\u0016J$\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\u001b\u001a\u00020\u000bH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001c\u0010\u0014J\f\u0010\u001d\u001a\u00020\t*\u00020\u001eH\u0002J\f\u0010\u001f\u001a\u00020\u001e*\u00020\tH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006 "}, d2 = {"Lcom/calsync/app/data/repository/CalendarRepository;", "", "calendarDao", "Lcom/calsync/app/data/local/database/CalendarDao;", "api", "Lcom/calsync/app/data/remote/api/CalSyncApi;", "(Lcom/calsync/app/data/local/database/CalendarDao;Lcom/calsync/app/data/remote/api/CalSyncApi;)V", "createCalendar", "Lkotlin/Result;", "Lcom/calsync/app/domain/model/Calendar;", "name", "", "color", "", "createCalendar-0E7RQCE", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteCalendar", "", "id", "deleteCalendar-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllCalendars", "Lkotlinx/coroutines/flow/Flow;", "", "getCalendarById", "getVisibleCalendars", "joinCalendar", "inviteCode", "joinCalendar-gIAlu-s", "toDomain", "Lcom/calsync/app/data/local/entity/CalendarEntity;", "toEntity", "app_debug"})
public final class CalendarRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.calsync.app.data.local.database.CalendarDao calendarDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.calsync.app.data.remote.api.CalSyncApi api = null;
    
    @javax.inject.Inject()
    public CalendarRepository(@org.jetbrains.annotations.NotNull()
    com.calsync.app.data.local.database.CalendarDao calendarDao, @org.jetbrains.annotations.NotNull()
    com.calsync.app.data.remote.api.CalSyncApi api) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.calsync.app.domain.model.Calendar>> getAllCalendars() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.calsync.app.domain.model.Calendar>> getVisibleCalendars() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getCalendarById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.calsync.app.domain.model.Calendar> $completion) {
        return null;
    }
    
    private final com.calsync.app.domain.model.Calendar toDomain(com.calsync.app.data.local.entity.CalendarEntity $this$toDomain) {
        return null;
    }
    
    private final com.calsync.app.data.local.entity.CalendarEntity toEntity(com.calsync.app.domain.model.Calendar $this$toEntity) {
        return null;
    }
}