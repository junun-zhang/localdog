package com.calsync.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calsync.app.data.local.TokenManager
import com.calsync.app.data.remote.api.CalSyncApi
import com.calsync.app.data.remote.model.LoginRequest
import com.calsync.app.data.remote.model.RegisterRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isLocalMode: Boolean = false,
    val error: String? = null,
    val nickname: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val api: CalSyncApi,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    init {
        val token = tokenManager.getToken()
        if (token != null) {
            val nickname = tokenManager.getNickname()
            _state.value = AuthUiState(isLoggedIn = true, nickname = nickname)
        } else {
            // Auto-enter local-only mode
            _state.value = AuthUiState(
                isLoggedIn = true,
                isLocalMode = true,
                nickname = "本地模式"
            )
        }
    }

    fun login(phone: String, password: String) {
        if (phone.isBlank() || password.isBlank()) {
            _state.value = _state.value.copy(error = "请填写手机号和密码")
            return
        }
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val response = api.login(LoginRequest(phone = phone, email = null, password = password))
                val body = response.body()
                if (response.isSuccessful && body?.success == true && body.data != null) {
                    val data = body.data
                    tokenManager.saveToken(data.token)
                    tokenManager.saveUserInfo(data.userId, data.nickname)
                    _state.value = AuthUiState(isLoggedIn = true, nickname = data.nickname)
                } else {
                    val msg = body?.message ?: "登录失败 (${response.code()})"
                    _state.value = _state.value.copy(isLoading = false, error = msg)
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = "网络错误: ${e.localizedMessage}")
            }
        }
    }

    fun register(phone: String, nickname: String, password: String) {
        if (phone.isBlank() || nickname.isBlank() || password.isBlank()) {
            _state.value = _state.value.copy(error = "请填写所有字段")
            return
        }
        if (password.length < 6) {
            _state.value = _state.value.copy(error = "密码至少 6 位")
            return
        }
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val response = api.register(RegisterRequest(phone = phone, email = null, nickname = nickname, password = password))
                val body = response.body()
                if (response.isSuccessful && body?.success == true && body.data != null) {
                    val data = body.data
                    tokenManager.saveToken(data.token)
                    tokenManager.saveUserInfo(data.userId, data.nickname)
                    _state.value = AuthUiState(isLoggedIn = true, nickname = data.nickname)
                } else {
                    val msg = body?.message ?: "注册失败 (${response.code()})"
                    _state.value = _state.value.copy(isLoading = false, error = msg)
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = "网络错误: ${e.localizedMessage}")
            }
        }
    }

    fun logout() {
        tokenManager.clear()
        _state.value = AuthUiState(
            isLoggedIn = true,
            isLocalMode = true,
            nickname = "本地模式"
        )
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
