package com.calsync.app.data.repository;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J$\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\tH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u000b\u0010\fJ$\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\b2\u0006\u0010\n\u001a\u00020\tH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u000f\u0010\fJ\u001a\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00120\u00112\u0006\u0010\u0013\u001a\u00020\u0014J\u0018\u0010\u0015\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0016\u001a\u00020\u0014H\u0086@\u00a2\u0006\u0002\u0010\u0017J*\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00120\u00112\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001aJ\u0014\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001d0\u0012H\u0086@\u00a2\u0006\u0002\u0010\u001eJ\u001a\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00120\u00112\u0006\u0010 \u001a\u00020\u0014J$\u0010!\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\tH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\"\u0010\fJ\f\u0010#\u001a\u00020$*\u00020\tH\u0002J\f\u0010%\u001a\u00020\t*\u00020\u001dH\u0002J\f\u0010&\u001a\u00020\u001d*\u00020\tH\u0002J\f\u0010\'\u001a\u00020(*\u00020\tH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006)"}, d2 = {"Lcom/calsync/app/data/repository/EventRepository;", "", "eventDao", "Lcom/calsync/app/data/local/database/EventDao;", "api", "Lcom/calsync/app/data/remote/api/CalSyncApi;", "(Lcom/calsync/app/data/local/database/EventDao;Lcom/calsync/app/data/remote/api/CalSyncApi;)V", "createEvent", "Lkotlin/Result;", "Lcom/calsync/app/domain/model/Event;", "event", "createEvent-gIAlu-s", "(Lcom/calsync/app/domain/model/Event;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteEvent", "", "deleteEvent-gIAlu-s", "getAllEvents", "Lkotlinx/coroutines/flow/Flow;", "", "calendarId", "", "getEventById", "id", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEventsInRange", "start", "", "end", "getUnsyncedEvents", "Lcom/calsync/app/data/local/entity/EventEntity;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchEvents", "query", "updateEvent", "updateEvent-gIAlu-s", "toCreateRequest", "Lcom/calsync/app/data/remote/model/CreateEventRequest;", "toDomain", "toEntity", "toUpdateRequest", "Lcom/calsync/app/data/remote/model/UpdateEventRequest;", "app_release"})
public final class EventRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.calsync.app.data.local.database.EventDao eventDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.calsync.app.data.remote.api.CalSyncApi api = null;
    
    @javax.inject.Inject()
    public EventRepository(@org.jetbrains.annotations.NotNull()
    com.calsync.app.data.local.database.EventDao eventDao, @org.jetbrains.annotations.NotNull()
    com.calsync.app.data.remote.api.CalSyncApi api) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.calsync.app.domain.model.Event>> getEventsInRange(@org.jetbrains.annotations.NotNull()
    java.lang.String calendarId, long start, long end) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.calsync.app.domain.model.Event>> getAllEvents(@org.jetbrains.annotations.NotNull()
    java.lang.String calendarId) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getEventById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.calsync.app.domain.model.Event> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.calsync.app.domain.model.Event>> searchEvents(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getUnsyncedEvents(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.calsync.app.data.local.entity.EventEntity>> $completion) {
        return null;
    }
    
    private final com.calsync.app.data.local.entity.EventEntity toEntity(com.calsync.app.domain.model.Event $this$toEntity) {
        return null;
    }
    
    private final com.calsync.app.domain.model.Event toDomain(com.calsync.app.data.local.entity.EventEntity $this$toDomain) {
        return null;
    }
    
    private final com.calsync.app.data.remote.model.CreateEventRequest toCreateRequest(com.calsync.app.domain.model.Event $this$toCreateRequest) {
        return null;
    }
    
    private final com.calsync.app.data.remote.model.UpdateEventRequest toUpdateRequest(com.calsync.app.domain.model.Event $this$toUpdateRequest) {
        return null;
    }
}