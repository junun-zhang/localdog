package com.example.ireader.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ireader.data.model.Resource
import com.example.ireader.data.model.User
import com.example.ireader.data.network.AuthApi
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
            val response = withContext(Dispatchers.IO) {
                authApi.login(email, password)
            }
            
            saveUser(response)
            Resource.success(response)
            
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
            val response = withContext(Dispatchers.IO) {
                authApi.register(email, password, name)
            }
            
            saveUser(response)
            Resource.success(response)
            
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
            .remove("user_token")
            .remove("user_email")
            .remove("user_name")
            .apply()
        
        _user.value = null
        _isAuthenticated.value = false
    }
    
    /**
     * 从 SharedPreferences 加载用户信息
     */
    private fun loadUserFromPrefs() {
        val token = sharedPreferences.getString("user_token", null)
        val email = sharedPreferences.getString("user_email", null)
        val name = sharedPreferences.getString("user_name", null)
        
        if (token != null && email != null) {
            _user.value = User(email = email, name = name ?: email, token = token)
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
            .putString("user_token", user.token)
            .putString("user_email", user.email)
            .putString("user_name", user.name)
            .apply()
        
        _user.value = user
        _isAuthenticated.value = true
    }
    
    /**
     * 获取认证头
     */
    fun getAuthHeader(): String? {
        val token = sharedPreferences.getString("user_token", null)
        return if (token != null) "Bearer $token" else null
    }
}