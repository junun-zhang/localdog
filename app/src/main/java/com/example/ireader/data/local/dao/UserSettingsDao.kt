package com.example.ireader.data.local.dao

import androidx.room.*
import com.example.ireader.data.local.entity.UserSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSettingsDao {
    @Query("SELECT * FROM user_settings WHERE `key` = :key")
    fun getSetting(key: String): Flow<UserSettings?>

    @Query("SELECT * FROM user_settings WHERE `key` = :key")
    suspend fun getSettingSync(key: String): UserSettings?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetting(setting: UserSettings)

    @Delete
    suspend fun deleteSetting(setting: UserSettings)

    @Query("SELECT * FROM user_settings")
    fun getAllSettings(): Flow<List<UserSettings>>
}
