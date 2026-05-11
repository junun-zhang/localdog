package com.calsync.app.di
import com.calsync.app.data.local.TokenManager
import com.calsync.app.data.remote.api.CalSyncApi
import com.calsync.app.data.remote.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "http://123.56.177.127:8080/"

    @Provides @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor, loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(authInterceptor).addInterceptor(loggingInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).build()

    @Provides @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.HEADERS }

    @Provides @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor =
        AuthInterceptor { tokenManager.getToken() }

    @Provides @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build()

    @Provides @Singleton
    fun provideCalSyncApi(retrofit: Retrofit): CalSyncApi = retrofit.create(CalSyncApi::class.java)
}
