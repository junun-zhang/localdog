package com.calsync.app.domain.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b,\b\u0086\b\u0018\u00002\u00020\u0001:\u0002@AB\u008f\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0012\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0014\u001a\u00020\b\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0016\u00a2\u0006\u0002\u0010\u0017J\t\u0010-\u001a\u00020\u0003H\u00c6\u0003J\t\u0010.\u001a\u00020\u0012H\u00c6\u0003J\t\u0010/\u001a\u00020\u0003H\u00c6\u0003J\t\u00100\u001a\u00020\bH\u00c6\u0003J\t\u00101\u001a\u00020\u0016H\u00c6\u0003J\t\u00102\u001a\u00020\u0003H\u00c6\u0003J\t\u00103\u001a\u00020\u0003H\u00c6\u0003J\u000b\u00104\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0010\u00105\u001a\u0004\u0018\u00010\bH\u00c6\u0003\u00a2\u0006\u0002\u0010\u001dJ\t\u00106\u001a\u00020\nH\u00c6\u0003J\t\u00107\u001a\u00020\fH\u00c6\u0003J\u000f\u00108\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u00c6\u0003J\u000b\u00109\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u009c\u0001\u0010:\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\f2\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0011\u001a\u00020\u00122\b\b\u0002\u0010\u0013\u001a\u00020\u00032\b\b\u0002\u0010\u0014\u001a\u00020\b2\b\b\u0002\u0010\u0015\u001a\u00020\u0016H\u00c6\u0001\u00a2\u0006\u0002\u0010;J\u0013\u0010<\u001a\u00020\u00122\b\u0010=\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010>\u001a\u00020\u0016H\u00d6\u0001J\t\u0010?\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u0013\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0019R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0019R\u0015\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\n\n\u0002\u0010\u001e\u001a\u0004\b\u001c\u0010\u001dR\u0013\u0010\u0010\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0019R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0019R\u0011\u0010\u0011\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010!R\u0011\u0010\u0014\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010)R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u0019R\u0011\u0010\u0015\u001a\u00020\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010,\u00a8\u0006B"}, d2 = {"Lcom/calsync/app/domain/model/Task;", "", "id", "", "calendarId", "title", "description", "dueDate", "", "priority", "Lcom/calsync/app/domain/model/Task$Priority;", "status", "Lcom/calsync/app/domain/model/Task$TaskStatus;", "reminders", "", "Lcom/calsync/app/domain/model/Event$Reminder;", "eventId", "isShared", "", "createdBy", "modifiedAt", "version", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Long;Lcom/calsync/app/domain/model/Task$Priority;Lcom/calsync/app/domain/model/Task$TaskStatus;Ljava/util/List;Ljava/lang/String;ZLjava/lang/String;JI)V", "getCalendarId", "()Ljava/lang/String;", "getCreatedBy", "getDescription", "getDueDate", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getEventId", "getId", "()Z", "getModifiedAt", "()J", "getPriority", "()Lcom/calsync/app/domain/model/Task$Priority;", "getReminders", "()Ljava/util/List;", "getStatus", "()Lcom/calsync/app/domain/model/Task$TaskStatus;", "getTitle", "getVersion", "()I", "component1", "component10", "component11", "component12", "component13", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Long;Lcom/calsync/app/domain/model/Task$Priority;Lcom/calsync/app/domain/model/Task$TaskStatus;Ljava/util/List;Ljava/lang/String;ZLjava/lang/String;JI)Lcom/calsync/app/domain/model/Task;", "equals", "other", "hashCode", "toString", "Priority", "TaskStatus", "app_debug"})
public final class Task {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String id = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String calendarId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String title = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String description = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long dueDate = null;
    @org.jetbrains.annotations.NotNull()
    private final com.calsync.app.domain.model.Task.Priority priority = null;
    @org.jetbrains.annotations.NotNull()
    private final com.calsync.app.domain.model.Task.TaskStatus status = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.calsync.app.domain.model.Event.Reminder> reminders = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String eventId = null;
    private final boolean isShared = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String createdBy = null;
    private final long modifiedAt = 0L;
    private final int version = 0;
    
    public Task(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String calendarId, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    java.lang.String description, @org.jetbrains.annotations.Nullable()
    java.lang.Long dueDate, @org.jetbrains.annotations.NotNull()
    com.calsync.app.domain.model.Task.Priority priority, @org.jetbrains.annotations.NotNull()
    com.calsync.app.domain.model.Task.TaskStatus status, @org.jetbrains.annotations.NotNull()
    java.util.List<com.calsync.app.domain.model.Event.Reminder> reminders, @org.jetbrains.annotations.Nullable()
    java.lang.String eventId, boolean isShared, @org.jetbrains.annotations.NotNull()
    java.lang.String createdBy, long modifiedAt, int version) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCalendarId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getDescription() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getDueDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.calsync.app.domain.model.Task.Priority getPriority() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.calsync.app.domain.model.Task.TaskStatus getStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.calsync.app.domain.model.Event.Reminder> getReminders() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getEventId() {
        return null;
    }
    
    public final boolean isShared() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCreatedBy() {
        return null;
    }
    
    public final long getModifiedAt() {
        return 0L;
    }
    
    public final int getVersion() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    public final boolean component10() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component11() {
        return null;
    }
    
    public final long component12() {
        return 0L;
    }
    
    public final int component13() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.calsync.app.domain.model.Task.Priority component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.calsync.app.domain.model.Task.TaskStatus component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.calsync.app.domain.model.Event.Reminder> component8() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.calsync.app.domain.model.Task copy(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String calendarId, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    java.lang.String description, @org.jetbrains.annotations.Nullable()
    java.lang.Long dueDate, @org.jetbrains.annotations.NotNull()
    com.calsync.app.domain.model.Task.Priority priority, @org.jetbrains.annotations.NotNull()
    com.calsync.app.domain.model.Task.TaskStatus status, @org.jetbrains.annotations.NotNull()
    java.util.List<com.calsync.app.domain.model.Event.Reminder> reminders, @org.jetbrains.annotations.Nullable()
    java.lang.String eventId, boolean isShared, @org.jetbrains.annotations.NotNull()
    java.lang.String createdBy, long modifiedAt, int version) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/calsync/app/domain/model/Task$Priority;", "", "(Ljava/lang/String;I)V", "NONE", "LOW", "MEDIUM", "HIGH", "app_debug"})
    public static enum Priority {
        /*public static final*/ NONE /* = new NONE() */,
        /*public static final*/ LOW /* = new LOW() */,
        /*public static final*/ MEDIUM /* = new MEDIUM() */,
        /*public static final*/ HIGH /* = new HIGH() */;
        
        Priority() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.calsync.app.domain.model.Task.Priority> getEntries() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2 = {"Lcom/calsync/app/domain/model/Task$TaskStatus;", "", "(Ljava/lang/String;I)V", "TODO", "IN_PROGRESS", "DONE", "app_debug"})
    public static enum TaskStatus {
        /*public static final*/ TODO /* = new TODO() */,
        /*public static final*/ IN_PROGRESS /* = new IN_PROGRESS() */,
        /*public static final*/ DONE /* = new DONE() */;
        
        TaskStatus() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.calsync.app.domain.model.Task.TaskStatus> getEntries() {
            return null;
        }
    }
}