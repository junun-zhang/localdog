package com.calsync.app.data.remote.api
import com.calsync.app.data.remote.model.*
import retrofit2.Response
import retrofit2.http.*

interface CalSyncApi {
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/auth/refresh")
    suspend fun refreshToken(@Body refreshToken: String): Response<AuthResponse>

    @GET("api/calendars")
    suspend fun getCalendars(): Response<List<CalendarDto>>

    @POST("api/calendars")
    suspend fun createCalendar(@Body request: CreateCalendarRequest): Response<CalendarDto>

    @GET("api/calendars/{id}")
    suspend fun getCalendar(@Path("id") id: String): Response<CalendarDto>

    @PUT("api/calendars/{id}")
    suspend fun updateCalendar(@Path("id") id: String, @Body request: CreateCalendarRequest): Response<CalendarDto>

    @DELETE("api/calendars/{id}")
    suspend fun deleteCalendar(@Path("id") id: String): Response<Unit>

    @POST("api/calendars/{id}/members")
    suspend fun inviteMember(@Path("id") id: String, @Body request: InviteRequest): Response<Unit>

    @GET("api/calendars/{id}/members")
    suspend fun getMembers(@Path("id") id: String): Response<List<MemberDto>>

    @DELETE("api/calendars/{id}/members/{userId}")
    suspend fun removeMember(@Path("id") id: String, @Path("userId") userId: String): Response<Unit>

    @POST("api/calendars/join")
    suspend fun joinCalendar(@Query("code") inviteCode: String): Response<CalendarDto>

    @GET("api/events")
    suspend fun getEvents(@Query("calendarId") calendarId: String, @Query("start") start: Long, @Query("end") end: Long): Response<List<EventDto>>

    @POST("api/events")
    suspend fun createEvent(@Body request: CreateEventRequest): Response<EventDto>

    @GET("api/events/{id}")
    suspend fun getEvent(@Path("id") id: String): Response<EventDto>

    @PUT("api/events/{id}")
    suspend fun updateEvent(@Path("id") id: String, @Body request: UpdateEventRequest): Response<EventDto>

    @DELETE("api/events/{id}")
    suspend fun deleteEvent(@Path("id") id: String): Response<Unit>

    @GET("api/tasks")
    suspend fun getTasks(@Query("calendarId") calendarId: String, @Query("status") status: Int?): Response<List<TaskDto>>

    @POST("api/tasks")
    suspend fun createTask(@Body request: CreateTaskRequest): Response<TaskDto>

    @PUT("api/tasks/{id}")
    suspend fun updateTask(@Path("id") id: String, @Body request: UpdateTaskRequest): Response<TaskDto>

    @DELETE("api/tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String): Response<Unit>

    @GET("api/search")
    suspend fun search(@Query("q") query: String, @Query("type") type: String?, @Query("start") start: Long?, @Query("end") end: Long?): Response<SearchResult>

    @GET("api/holidays")
    suspend fun getHolidays(@Query("year") year: Int, @Query("month") month: Int?): Response<List<HolidayDto>>

    @GET("api/holidays/school")
    suspend fun getSchoolCalendar(@Query("region") region: String): Response<List<HolidayDto>>

    @GET("api/weather")
    suspend fun getWeather(@Query("lat") lat: Double, @Query("lon") lon: Double): Response<WeatherDto>
}
