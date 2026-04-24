package com.calsync.app.data.remote.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00aa\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u001e\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0007J\u001e\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u000bJ\u001e\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u000fJ\u001e\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u00032\b\b\u0001\u0010\u0012\u001a\u00020\u0013H\u00a7@\u00a2\u0006\u0002\u0010\u0014J\u001e\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00110\u00032\b\b\u0001\u0010\u0012\u001a\u00020\u0013H\u00a7@\u00a2\u0006\u0002\u0010\u0014J\u001e\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00110\u00032\b\b\u0001\u0010\u0012\u001a\u00020\u0013H\u00a7@\u00a2\u0006\u0002\u0010\u0014J\u001e\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0012\u001a\u00020\u0013H\u00a7@\u00a2\u0006\u0002\u0010\u0014J\u001a\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u00190\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u001aJ\u001e\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\t0\u00032\b\b\u0001\u0010\u0012\u001a\u00020\u0013H\u00a7@\u00a2\u0006\u0002\u0010\u0014J8\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00190\u00032\b\b\u0001\u0010\u001d\u001a\u00020\u00132\b\b\u0001\u0010\u001e\u001a\u00020\u001f2\b\b\u0001\u0010 \u001a\u00020\u001fH\u00a7@\u00a2\u0006\u0002\u0010!J0\u0010\"\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020#0\u00190\u00032\b\b\u0001\u0010$\u001a\u00020%2\n\b\u0001\u0010&\u001a\u0004\u0018\u00010%H\u00a7@\u00a2\u0006\u0002\u0010\'J$\u0010(\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020)0\u00190\u00032\b\b\u0001\u0010\u0012\u001a\u00020\u0013H\u00a7@\u00a2\u0006\u0002\u0010\u0014J$\u0010*\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020#0\u00190\u00032\b\b\u0001\u0010+\u001a\u00020\u0013H\u00a7@\u00a2\u0006\u0002\u0010\u0014J0\u0010,\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u00190\u00032\b\b\u0001\u0010\u001d\u001a\u00020\u00132\n\b\u0001\u0010-\u001a\u0004\u0018\u00010%H\u00a7@\u00a2\u0006\u0002\u0010.J(\u0010/\u001a\b\u0012\u0004\u0012\u0002000\u00032\b\b\u0001\u00101\u001a\u0002022\b\b\u0001\u00103\u001a\u000202H\u00a7@\u00a2\u0006\u0002\u00104J(\u00105\u001a\b\u0012\u0004\u0012\u00020\u00110\u00032\b\b\u0001\u0010\u0012\u001a\u00020\u00132\b\b\u0001\u0010\u0005\u001a\u000206H\u00a7@\u00a2\u0006\u0002\u00107J\u001e\u00108\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u00109\u001a\u00020\u0013H\u00a7@\u00a2\u0006\u0002\u0010\u0014J\u001e\u0010:\u001a\b\u0012\u0004\u0012\u00020;0\u00032\b\b\u0001\u0010\u0005\u001a\u00020<H\u00a7@\u00a2\u0006\u0002\u0010=J\u001e\u0010>\u001a\b\u0012\u0004\u0012\u00020;0\u00032\b\b\u0001\u0010>\u001a\u00020\u0013H\u00a7@\u00a2\u0006\u0002\u0010\u0014J\u001e\u0010?\u001a\b\u0012\u0004\u0012\u00020;0\u00032\b\b\u0001\u0010\u0005\u001a\u00020@H\u00a7@\u00a2\u0006\u0002\u0010AJ(\u0010B\u001a\b\u0012\u0004\u0012\u00020\u00110\u00032\b\b\u0001\u0010\u0012\u001a\u00020\u00132\b\b\u0001\u0010C\u001a\u00020\u0013H\u00a7@\u00a2\u0006\u0002\u0010DJB\u0010E\u001a\b\u0012\u0004\u0012\u00020F0\u00032\b\b\u0001\u0010G\u001a\u00020\u00132\n\b\u0001\u0010H\u001a\u0004\u0018\u00010\u00132\n\b\u0001\u0010\u001e\u001a\u0004\u0018\u00010\u001f2\n\b\u0001\u0010 \u001a\u0004\u0018\u00010\u001fH\u00a7@\u00a2\u0006\u0002\u0010IJ(\u0010J\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0012\u001a\u00020\u00132\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010KJ(\u0010L\u001a\b\u0012\u0004\u0012\u00020\t0\u00032\b\b\u0001\u0010\u0012\u001a\u00020\u00132\b\b\u0001\u0010\u0005\u001a\u00020MH\u00a7@\u00a2\u0006\u0002\u0010NJ(\u0010O\u001a\b\u0012\u0004\u0012\u00020\r0\u00032\b\b\u0001\u0010\u0012\u001a\u00020\u00132\b\b\u0001\u0010\u0005\u001a\u00020PH\u00a7@\u00a2\u0006\u0002\u0010Q\u00a8\u0006R"}, d2 = {"Lcom/calsync/app/data/remote/api/CalSyncApi;", "", "createCalendar", "Lretrofit2/Response;", "Lcom/calsync/app/data/remote/model/CalendarDto;", "request", "Lcom/calsync/app/data/remote/model/CreateCalendarRequest;", "(Lcom/calsync/app/data/remote/model/CreateCalendarRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createEvent", "Lcom/calsync/app/data/remote/model/EventDto;", "Lcom/calsync/app/data/remote/model/CreateEventRequest;", "(Lcom/calsync/app/data/remote/model/CreateEventRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createTask", "Lcom/calsync/app/data/remote/model/TaskDto;", "Lcom/calsync/app/data/remote/model/CreateTaskRequest;", "(Lcom/calsync/app/data/remote/model/CreateTaskRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteCalendar", "", "id", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteEvent", "deleteTask", "getCalendar", "getCalendars", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEvent", "getEvents", "calendarId", "start", "", "end", "(Ljava/lang/String;JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getHolidays", "Lcom/calsync/app/data/remote/model/HolidayDto;", "year", "", "month", "(ILjava/lang/Integer;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMembers", "Lcom/calsync/app/data/remote/model/MemberDto;", "getSchoolCalendar", "region", "getTasks", "status", "(Ljava/lang/String;Ljava/lang/Integer;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getWeather", "Lcom/calsync/app/data/remote/model/WeatherDto;", "lat", "", "lon", "(DDLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "inviteMember", "Lcom/calsync/app/data/remote/model/InviteRequest;", "(Ljava/lang/String;Lcom/calsync/app/data/remote/model/InviteRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "joinCalendar", "inviteCode", "login", "Lcom/calsync/app/data/remote/model/AuthResponse;", "Lcom/calsync/app/data/remote/model/LoginRequest;", "(Lcom/calsync/app/data/remote/model/LoginRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "refreshToken", "register", "Lcom/calsync/app/data/remote/model/RegisterRequest;", "(Lcom/calsync/app/data/remote/model/RegisterRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "removeMember", "userId", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "search", "Lcom/calsync/app/data/remote/model/SearchResult;", "query", "type", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/Long;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateCalendar", "(Ljava/lang/String;Lcom/calsync/app/data/remote/model/CreateCalendarRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateEvent", "Lcom/calsync/app/data/remote/model/UpdateEventRequest;", "(Ljava/lang/String;Lcom/calsync/app/data/remote/model/UpdateEventRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateTask", "Lcom/calsync/app/data/remote/model/UpdateTaskRequest;", "(Ljava/lang/String;Lcom/calsync/app/data/remote/model/UpdateTaskRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"})
public abstract interface CalSyncApi {
    
    @retrofit2.http.POST(value = "api/auth/register")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object register(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.calsync.app.data.remote.model.RegisterRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.calsync.app.data.remote.model.AuthResponse>> $completion);
    
    @retrofit2.http.POST(value = "api/auth/login")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object login(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.calsync.app.data.remote.model.LoginRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.calsync.app.data.remote.model.AuthResponse>> $completion);
    
    @retrofit2.http.POST(value = "api/auth/refresh")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object refreshToken(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    java.lang.String refreshToken, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.calsync.app.data.remote.model.AuthResponse>> $completion);
    
    @retrofit2.http.GET(value = "api/calendars")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCalendars(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.calsync.app.data.remote.model.CalendarDto>>> $completion);
    
    @retrofit2.http.POST(value = "api/calendars")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object createCalendar(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.calsync.app.data.remote.model.CreateCalendarRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.calsync.app.data.remote.model.CalendarDto>> $completion);
    
    @retrofit2.http.GET(value = "api/calendars/{id}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCalendar(@retrofit2.http.Path(value = "id")
    @org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.calsync.app.data.remote.model.CalendarDto>> $completion);
    
    @retrofit2.http.PUT(value = "api/calendars/{id}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateCalendar(@retrofit2.http.Path(value = "id")
    @org.jetbrains.annotations.NotNull()
    java.lang.String id, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.calsync.app.data.remote.model.CreateCalendarRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.calsync.app.data.remote.model.CalendarDto>> $completion);
    
    @retrofit2.http.DELETE(value = "api/calendars/{id}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteCalendar(@retrofit2.http.Path(value = "id")
    @org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<kotlin.Unit>> $completion);
    
    @retrofit2.http.POST(value = "api/calendars/{id}/members")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object inviteMember(@retrofit2.http.Path(value = "id")
    @org.jetbrains.annotations.NotNull()
    java.lang.String id, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.calsync.app.data.remote.model.InviteRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<kotlin.Unit>> $completion);
    
    @retrofit2.http.GET(value = "api/calendars/{id}/members")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMembers(@retrofit2.http.Path(value = "id")
    @org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.calsync.app.data.remote.model.MemberDto>>> $completion);
    
    @retrofit2.http.DELETE(value = "api/calendars/{id}/members/{userId}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object removeMember(@retrofit2.http.Path(value = "id")
    @org.jetbrains.annotations.NotNull()
    java.lang.String id, @retrofit2.http.Path(value = "userId")
    @org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<kotlin.Unit>> $completion);
    
    @retrofit2.http.POST(value = "api/calendars/join")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object joinCalendar(@retrofit2.http.Query(value = "code")
    @org.jetbrains.annotations.NotNull()
    java.lang.String inviteCode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.calsync.app.data.remote.model.CalendarDto>> $completion);
    
    @retrofit2.http.GET(value = "api/events")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEvents(@retrofit2.http.Query(value = "calendarId")
    @org.jetbrains.annotations.NotNull()
    java.lang.String calendarId, @retrofit2.http.Query(value = "start")
    long start, @retrofit2.http.Query(value = "end")
    long end, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.calsync.app.data.remote.model.EventDto>>> $completion);
    
    @retrofit2.http.POST(value = "api/events")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object createEvent(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.calsync.app.data.remote.model.CreateEventRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.calsync.app.data.remote.model.EventDto>> $completion);
    
    @retrofit2.http.GET(value = "api/events/{id}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEvent(@retrofit2.http.Path(value = "id")
    @org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.calsync.app.data.remote.model.EventDto>> $completion);
    
    @retrofit2.http.PUT(value = "api/events/{id}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateEvent(@retrofit2.http.Path(value = "id")
    @org.jetbrains.annotations.NotNull()
    java.lang.String id, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.calsync.app.data.remote.model.UpdateEventRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.calsync.app.data.remote.model.EventDto>> $completion);
    
    @retrofit2.http.DELETE(value = "api/events/{id}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteEvent(@retrofit2.http.Path(value = "id")
    @org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<kotlin.Unit>> $completion);
    
    @retrofit2.http.GET(value = "api/tasks")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTasks(@retrofit2.http.Query(value = "calendarId")
    @org.jetbrains.annotations.NotNull()
    java.lang.String calendarId, @retrofit2.http.Query(value = "status")
    @org.jetbrains.annotations.Nullable()
    java.lang.Integer status, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.calsync.app.data.remote.model.TaskDto>>> $completion);
    
    @retrofit2.http.POST(value = "api/tasks")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object createTask(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.calsync.app.data.remote.model.CreateTaskRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.calsync.app.data.remote.model.TaskDto>> $completion);
    
    @retrofit2.http.PUT(value = "api/tasks/{id}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateTask(@retrofit2.http.Path(value = "id")
    @org.jetbrains.annotations.NotNull()
    java.lang.String id, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.calsync.app.data.remote.model.UpdateTaskRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.calsync.app.data.remote.model.TaskDto>> $completion);
    
    @retrofit2.http.DELETE(value = "api/tasks/{id}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteTask(@retrofit2.http.Path(value = "id")
    @org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<kotlin.Unit>> $completion);
    
    @retrofit2.http.GET(value = "api/search")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object search(@retrofit2.http.Query(value = "q")
    @org.jetbrains.annotations.NotNull()
    java.lang.String query, @retrofit2.http.Query(value = "type")
    @org.jetbrains.annotations.Nullable()
    java.lang.String type, @retrofit2.http.Query(value = "start")
    @org.jetbrains.annotations.Nullable()
    java.lang.Long start, @retrofit2.http.Query(value = "end")
    @org.jetbrains.annotations.Nullable()
    java.lang.Long end, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.calsync.app.data.remote.model.SearchResult>> $completion);
    
    @retrofit2.http.GET(value = "api/holidays")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getHolidays(@retrofit2.http.Query(value = "year")
    int year, @retrofit2.http.Query(value = "month")
    @org.jetbrains.annotations.Nullable()
    java.lang.Integer month, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.calsync.app.data.remote.model.HolidayDto>>> $completion);
    
    @retrofit2.http.GET(value = "api/holidays/school")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSchoolCalendar(@retrofit2.http.Query(value = "region")
    @org.jetbrains.annotations.NotNull()
    java.lang.String region, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.calsync.app.data.remote.model.HolidayDto>>> $completion);
    
    @retrofit2.http.GET(value = "api/weather")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getWeather(@retrofit2.http.Query(value = "lat")
    double lat, @retrofit2.http.Query(value = "lon")
    double lon, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.calsync.app.data.remote.model.WeatherDto>> $completion);
}