package com.example.ireader.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ireader.data.model.Resource
import com.example.ireader.data.model.User
import com.example.ireader.data.network.AuthApi
import com.example.ireader.data.network.LoginRequest
import com.example.ireader.data.network.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 用户认证仓库，负责处理登录、注册和用户状态管理
 */
@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    context: Context
) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated: LiveData<Boolean> = _isAuthenticated

    init {
        // 初始化用户状态
        loadUserFromPrefs()
    }

    /**
     * 用户登录
     */
    suspend fun login(email: String, password: String): Resource<User> {
        return try {
            // 注意：LoginRequest 实际上期望 username，但我们传递 email
            // 这里可能存在 API 设计问题，但先按现有代码处理
            val loginRequest = LoginRequest(username = email, password = password)
            val response = withContext(Dispatchers.IO) {
                authApi.login(loginRequest)
            }

            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!
                // User 没有 token 字段，所以不能保存 token
                // 只保存用户信息到内存，token 需要从其他方式获取
                _user.value = user
                _isAuthenticated.value = true
                Resource.success(user)
            } else {
                Resource.error("登录失败: ${response.message()}", null)
            }

        } catch (e: HttpException) {
            Resource.error("登录失败: ${e.message()}", null)
        } catch (e: IOException) {
            Resource.error("网络连接失败", null)
        } catch (e: Exception) {
            Resource.error("未知错误: ${e.message}", null)
        }
    }

    /**
     * 用户注册
     */
    suspend fun register(email: String, password: String, name: String): Resource<User> {
        return try {
            // RegisterRequest 期望 username，但我们有 name 参数
            val registerRequest = RegisterRequest(username = name, email = email, password = password)
            val response = withContext(Dispatchers.IO) {
                authApi.register(registerRequest)
            }

            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!
                _user.value = user
                _isAuthenticated.value = true
                Resource.success(user)
            } else {
                Resource.error("注册失败: ${response.message()}", null)
            }

        } catch (e: HttpException) {
            Resource.error("注册失败: ${e.message()}", null)
        } catch (e: IOException) {
            Resource.error("网络连接失败", null)
        } catch (e: Exception) {
            Resource.error("未知错误: ${e.message}", null)
        }
    }

    /**
     * 用户登出
     */
    fun logout() {
        sharedPreferences.edit()
            .remove("user_id")
            .remove("user_username")
            .remove("user_email")
            .remove("user_avatar_url")
            .remove("user_created_at")
            .remove("user_last_login_at")
            .apply()

        _user.value = null
        _isAuthenticated.value = false
    }

    /**
     * 从 SharedPreferences 加载用户信息
     */
    private fun loadUserFromPrefs() {
        val id = sharedPreferences.getString("user_id", null)
        val username = sharedPreferences.getString("user_username", null)
        val email = sharedPreferences.getString("user_email", null)
        val avatarUrl = sharedPreferences.getString("user_avatar_url", null)
        val createdAt = sharedPreferences.getLong("user_created_at", 0L)
        val lastLoginAt = sharedPreferences.getLong("user_last_login_at", 0L)

        if (id != null && username != null && email != null) {
            _user.value = User(
                id = id,
                username = username,
                email = email,
                avatarUrl = avatarUrl,
                createdAt = createdAt,
                lastLoginAt = lastLoginAt
            )
            _isAuthenticated.value = true
        } else {
            _user.value = null
            _isAuthenticated.value = false
        }
    }

    /**
     * 保存用户信息到 SharedPreferences
     */
    private fun saveUser(user: User) {
        sharedPreferences.edit()
            .putString("user_id", user.id)
            .putString("user_username", user.username)
            .putString("user_email", user.email)
            .putString("user_avatar_url", user.avatarUrl)
            .putLong("user_created_at", user.createdAt)
            .putLong("user_last_login_at", user.lastLoginAt)
            .apply()

        _user.value = user
        _isAuthenticated.value = true
    }

    /**
     * 获取认证头 - 由于 User 没有 token，这里返回 null
     * 实际项目中可能需要单独存储 token
     */
    fun getAuthHeader(): String? {
        // 由于当前 User 模型没有 token 字段，无法获取 token
        // 这表明数据模型设计存在问题
        return null
    }
}