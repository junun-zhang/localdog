package com.calsync.app.data.repository;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u000bJ$\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\u0006\u0010\u000f\u001a\u00020\u000eH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0010\u0010\u0011J$\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\b0\r2\u0006\u0010\u000f\u001a\u00020\u000eH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0013\u0010\u0011J\u0018\u0010\u0014\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u000bJ\u001a\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00170\u00162\u0006\u0010\u0018\u001a\u00020\nJ\u001a\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00170\u00162\u0006\u0010\u001a\u001a\u00020\u001bJ\u001a\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00170\u00162\u0006\u0010\u001d\u001a\u00020\nJ$\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\u0006\u0010\u000f\u001a\u00020\u000eH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001f\u0010\u0011J\f\u0010 \u001a\u00020!*\u00020\u000eH\u0002J\f\u0010\"\u001a\u00020\u000e*\u00020#H\u0002J\f\u0010$\u001a\u00020#*\u00020\u000eH\u0002J\f\u0010%\u001a\u00020&*\u00020\u000eH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\'"}, d2 = {"Lcom/calsync/app/data/repository/TaskRepository;", "", "taskDao", "Lcom/calsync/app/data/local/database/TaskDao;", "api", "Lcom/calsync/app/data/remote/api/CalSyncApi;", "(Lcom/calsync/app/data/local/database/TaskDao;Lcom/calsync/app/data/remote/api/CalSyncApi;)V", "completeTask", "", "id", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createTask", "Lkotlin/Result;", "Lcom/calsync/app/domain/model/Task;", "task", "createTask-gIAlu-s", "(Lcom/calsync/app/domain/model/Task;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteTask", "deleteTask-gIAlu-s", "getTaskById", "getTasksByCalendar", "Lkotlinx/coroutines/flow/Flow;", "", "calendarId", "getTasksByStatus", "status", "", "searchTasks", "query", "updateTask", "updateTask-gIAlu-s", "toCreateRequest", "Lcom/calsync/app/data/remote/model/CreateTaskRequest;", "toDomain", "Lcom/calsync/app/data/local/entity/TaskEntity;", "toEntity", "toUpdateRequest", "Lcom/calsync/app/data/remote/model/UpdateTaskRequest;", "app_debug"})
public final class TaskRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.calsync.app.data.local.database.TaskDao taskDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.calsync.app.data.remote.api.CalSyncApi api = null;
    
    @javax.inject.Inject()
    public TaskRepository(@org.jetbrains.annotations.NotNull()
    com.calsync.app.data.local.database.TaskDao taskDao, @org.jetbrains.annotations.NotNull()
    com.calsync.app.data.remote.api.CalSyncApi api) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.calsync.app.domain.model.Task>> getTasksByCalendar(@org.jetbrains.annotations.NotNull()
    java.lang.String calendarId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.calsync.app.domain.model.Task>> getTasksByStatus(int status) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getTaskById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.calsync.app.domain.model.Task> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.calsync.app.domain.model.Task>> searchTasks(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object completeTask(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final com.calsync.app.data.local.entity.TaskEntity toEntity(com.calsync.app.domain.model.Task $this$toEntity) {
        return null;
    }
    
    private final com.calsync.app.domain.model.Task toDomain(com.calsync.app.data.local.entity.TaskEntity $this$toDomain) {
        return null;
    }
    
    private final com.calsync.app.data.remote.model.CreateTaskRequest toCreateRequest(com.calsync.app.domain.model.Task $this$toCreateRequest) {
        return null;
    }
    
    private final com.calsync.app.data.remote.model.UpdateTaskRequest toUpdateRequest(com.calsync.app.domain.model.Task $this$toUpdateRequest) {
        return null;
    }
}