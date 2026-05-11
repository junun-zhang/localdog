package com.calsync.app.data.remote.api

import com.calsync.app.data.remote.model.*
import retrofit2.Response
import retrofit2.http.*

interface CalSyncApi {
    // ---- Auth ----
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<AuthResponse>>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<AuthResponse>>

    @GET("api/auth/me")
    suspend fun getMe(): Response<ApiResponse<AuthResponse>>

    // ---- Calendars ----
    @GET("api/calendars")
    suspend fun getCalendars(): Response<ApiResponse<List<CalendarDto>>>

    @POST("api/calendars")
    suspend fun createCalendar(@Body request: CreateCalendarRequest): Response<ApiResponse<CalendarDto>>

    @GET("api/calendars/{id}")
    suspend fun getCalendar(@Path("id") id: String): Response<ApiResponse<CalendarDto>>

    @PUT("api/calendars/{id}")
    suspend fun updateCalendar(@Path("id") id: String, @Body request: CreateCalendarRequest): Response<ApiResponse<CalendarDto>>

    @DELETE("api/calendars/{id}")
    suspend fun deleteCalendar(@Path("id") id: String): Response<ApiResponse<Unit>>

    @POST("api/calendars/{id}/members")
    suspend fun inviteMember(@Path("id") id: String): Response<ApiResponse<String>>

    @GET("api/calendars/{id}/members")
    suspend fun getMembers(@Path("id") id: String): Response<ApiResponse<List<MemberDto>>>

    @POST("api/calendars/join")
    suspend fun joinCalendar(@Body request: JoinRequest): Response<ApiResponse<CalendarDto>>

    // ---- Events ----
    @GET("api/events")
    suspend fun getEvents(
        @Query("calendarId") calendarId: String,
        @Query("start") start: Long,
        @Query("end") end: Long
    ): Response<ApiResponse<List<EventDto>>>

    @POST("api/events")
    suspend fun createEvent(
        @Query("calendarId") calendarId: String,
        @Body request: CreateEventRequest
    ): Response<ApiResponse<EventDto>>

    @GET("api/events/{id}")
    suspend fun getEvent(@Path("id") id: String): Response<ApiResponse<EventDto>>

    @PUT("api/events/{id}")
    suspend fun updateEvent(@Path("id") id: String, @Body request: UpdateEventRequest): Response<ApiResponse<EventDto>>

    @DELETE("api/events/{id}")
    suspend fun deleteEvent(@Path("id") id: String): Response<ApiResponse<Unit>>

    // ---- Tasks ----
    @GET("api/tasks")
    suspend fun getTasks(
        @Query("calendarId") calendarId: String,
        @Query("status") status: Int?
    ): Response<ApiResponse<List<TaskDto>>>

    @POST("api/tasks")
    suspend fun createTask(@Body request: CreateTaskRequest): Response<ApiResponse<TaskDto>>

    @PUT("api/tasks/{id}")
    suspend fun updateTask(@Path("id") id: String, @Body request: UpdateTaskRequest): Response<ApiResponse<TaskDto>>

    @DELETE("api/tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String): Response<ApiResponse<Unit>>

    // ---- Search ----
    @GET("api/search")
    suspend fun search(
        @Query("q") query: String,
        @Query("type") type: String?,
        @Query("start") start: Long?,
        @Query("end") end: Long?
    ): Response<ApiResponse<SearchResult>>

    // ---- Holidays ----
    @GET("api/holidays")
    suspend fun getHolidays(
        @Query("year") year: Int,
        @Query("month") month: Int?
    ): Response<ApiResponse<List<HolidayDto>>>

    @GET("api/holidays/school")
    suspend fun getSchoolCalendar(@Query("region") region: String): Response<ApiResponse<List<HolidayDto>>>

    // ---- Weather ----
    @GET("api/weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Response<ApiResponse<WeatherDto>>
}
